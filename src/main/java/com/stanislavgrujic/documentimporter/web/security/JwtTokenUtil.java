package com.stanislavgrujic.documentimporter.web.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.stanislavgrujic.documentimporter.model.User;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {

  // currently set to 24 hours
  private static final int JWT_EXPIRATION_TIME = 24 * 60 * 60 * 1000;

  private static final String SECRET = "16F3FB40F1E3C8C63C014C64D5BA11E7";

  private JWSSigner signer;
  private JWSVerifier verifier;

  @PostConstruct
  private void configure() {
    createSigner();
    createVerifier();
  }

  private void createSigner() {
    try {
      this.signer = new MACSigner(SECRET);
    } catch (KeyLengthException e) {
      throw new RuntimeException("Secret key is not configured properly", e);
    }
  }

  private void createVerifier() {
    try {
      this.verifier = new MACVerifier(SECRET);
    } catch (JOSEException e) {
      throw new RuntimeException("Secret key is not configured properly", e);
    }
  }

  public String createToken(User user) {
    Date issueTime = new Date();
    JWTClaimsSet claims = new JWTClaimsSet.Builder()
        .subject(user.getEmail())
        .claim("email", user.getEmail())
        .claim("name", user.getFullName())
        .claim("role", user.getRole())
        .issueTime(issueTime)
        .expirationTime(new Date(issueTime.getTime() + JWT_EXPIRATION_TIME)).build();

    Payload payload = new Payload(claims.toJSONObject());
    return encryptJws(payload);
  }

  private String encryptJws(Payload payload) {
    try {
      JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
      JWSObject jwsObject = new JWSObject(jwsHeader, payload);
      jwsObject.sign(signer);
      return jwsObject.serialize();
    } catch (JOSEException e) {
      throw new RuntimeException("Could not sign JWT object payload", e);
    }
  }

  public String validate(String token) {
    if (StringUtils.isEmpty(token)) {
      return null;
    }

    String[] jwsParts = token.split("\\.");
    try {
      Base64URL firstPart = new Base64URL(jwsParts[0]);
      Base64URL secondPart = new Base64URL(jwsParts[1]);
      Base64URL thirdPart = new Base64URL(jwsParts[2]);
      JWSObject jwsObject = new JWSObject(firstPart, secondPart, thirdPart);
      jwsObject.verify(verifier);

      JSONObject jsonObject = jwsObject.getPayload().toJSONObject();
      JWTClaimsSet claims = JWTClaimsSet.parse(jsonObject);

      Date expirationTime = claims.getExpirationTime();
      if (expirationTime.before(new Date())) {
        throw new AccessForbiddenException();
      }

      return claims.getSubject();
    } catch (ParseException | JOSEException e) {
      throw new AccessForbiddenException();
    }
  }

}
