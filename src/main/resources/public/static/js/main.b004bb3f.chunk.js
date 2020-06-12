(this["webpackJsonparchitect-notes"]=this["webpackJsonparchitect-notes"]||[]).push([[0],{100:function(e,t,n){"use strict";n.r(t);var a=n(0),r=n.n(a),c=n(55),o=n.n(c),u=n(1),i=n(18),l=n(12),s=n(2),b=n(4),m=n(11),d=n.n(m),f=n(56),p=n.n(f).a.create({baseURL:"http://localhost:5000/api",paramsSerializer:d.a.stringify}),v=n(33),h=function(e){var t=[];return e.topicList.forEach((function(e){if(e.parentId){var n=t.findIndex((function(t){return t.id===e.parentId}));void 0!==n&&t.splice(n+1,0,Object(v.a)({},e,{title:e.title.trim(),depth:t[n].depth+1}))}else t.push(Object(v.a)({},e,{title:e.title.trim(),depth:0}))})),t},E=["entry","medium","advanced"],j=function(e){var t=e.loadingState,n=e.children;return r.a.createElement(r.a.Fragment,null,"success"===t?n:r.a.createElement("p",null,r.a.createElement("em",null,"loading"===t?"Loading...":"An error ocurred :(")))},O=n(57),g=n.n(O),k=n(58);function y(){var e=Object(u.a)(["\n  * {\n    max-width: 100%;\n  }\n"]);return y=function(){return e},e}var x=s.b.div(y()),S=Object(a.memo)((function(e){var t=e.input;return r.a.createElement(x,{dangerouslySetInnerHTML:{__html:Object(k.sanitize)(g()(t))}})})),w=n(62),C=n.n(w);function L(){var e=Object(u.a)(["\n  width: 640px;\n  height: 360px;\n  display: flex;\n  align-items: center;\n  justify-content: center;\n  cursor: pointer;\n  background: black;\n  color: white;\n"]);return L=function(){return e},e}function I(){var e=Object(u.a)(["\n  margin-top: 20px;\n  display: flex;\n  justify-content: center;\n"]);return I=function(){return e},e}var P=s.b.div(I()),R=s.b.div(L()),A=function(e){var t=e.split(":"),n=Object(b.a)(t,3),a=n[0],r=n[1],c=n[2];return 60*parseInt(a,10)*60+60*parseInt(r,10)+parseInt(c,10)},U=Object(a.memo)((function(e){var t=e.sourceLink,n=e.timePeriod,c=Object(a.useState)(!1),o=Object(b.a)(c,2),u=o[0],i=o[1],l=Object(a.useCallback)((function(){i(!0)}),[i]),s=function(e){if(!e)return[0,1/0];var t=e.split(" - "),n=Object(b.a)(t,2),a=n[0],r=n[1];return[A(a),A(r)]}(n),m=Object(b.a)(s,2),f=m[0],p=m[1],v=function(e){var t;try{t=new URL(e)}catch(a){t=new URL(e.match(/[^[](\S+)(?=])/gi)[0])}var n=t.search;return d.a.parse(n.slice(1)).v}(t),h=Object(a.useRef)(null),E=Object(a.useRef)(!1),j=Object(a.useCallback)((function(e){var t;e.playedSeconds>p&&e.playedSeconds-p<2&&!E.current&&((null===(t=h.current)||void 0===t?void 0:t.getInternalPlayer()).pauseVideo(),E.current=!0)}),[p,E]);return r.a.createElement(P,null,u?r.a.createElement(C.a,{ref:h,url:"https://www.youtube.com/watch?v=".concat(v,"&start=").concat(f),controls:!0,onProgress:j}):r.a.createElement(R,{onClick:l},"Show video"))}));function z(){var e=Object(u.a)(["\n  font-size: 0.7em;\n"]);return z=function(){return e},e}function T(){var e=Object(u.a)(["\n  opacity: 0.75;\n  font-size: 0.8em;\n  padding-bottom: 5px;\n  border-bottom: 1px solid black;\n"]);return T=function(){return e},e}function B(){var e=Object(u.a)(["\n  display: flex;\n  justify-content: space-between;\n"]);return B=function(){return e},e}function F(){var e=Object(u.a)(["\n  margin: 0;\n"]);return F=function(){return e},e}function J(){var e=Object(u.a)(["\n  border: 1px solid black;\n  padding: 10px;\n  border-radius: 8px;\n  margin-bottom: 15px;\n"]);return J=function(){return e},e}var M=s.b.div(J()),N=s.b.p(F()),_=s.b.div(B()),D=s.b.p(T()),H=s.b.span(z()),V=Object(a.memo)((function(e){var t,n=e.result;return r.a.createElement(M,null,r.a.createElement(_,null,r.a.createElement(N,null,"Category: ",n.attributes.semantics),r.a.createElement(N,null,r.a.createElement(i.b,{to:"/knowledge-block?id=".concat(n.id)},"Permalink"))),r.a.createElement(D,null,n.path.slice(1).map((function(e){return e.title})).join(" - ")),r.a.createElement(_,null,n.attributes.sourceAuthor&&r.a.createElement(H,null,"Author: ",n.attributes.sourceAuthor),n.attributes.timePeriod&&r.a.createElement(H,null,"Relevant section: ",n.attributes.timePeriod)),r.a.createElement(S,{input:n.text}),(null===(t=n.attributes.sourceLink)||void 0===t?void 0:t.includes("youtube.com"))&&r.a.createElement(U,{sourceLink:n.attributes.sourceLink,timePeriod:n.attributes.timePeriod}))}));function q(){var e=Object(u.a)(["\n  margin: 0 auto;\n  width: 800px;\n"]);return q=function(){return e},e}var G=s.b.div(q()),K=function(){var e=Object(l.h)().search,t=d.a.parse(e.slice(1)).id,n=Object(a.useState)(null),c=Object(b.a)(n,2),o=c[0],u=c[1],i=Object(a.useState)("loading"),s=Object(b.a)(i,2),m=s[0],f=s[1];return Object(a.useEffect)((function(){(function(e){return p.get("knowledgeblocks/".concat(encodeURI(e))).then((function(e){return e.data}))})(t).then(u).then((function(){return f("success")})).catch((function(e){console.error(e),f("error")}))}),[t]),r.a.createElement(G,null,r.a.createElement(j,{loadingState:m},r.a.createElement(V,{result:o})))},Q=Object(a.memo)((function(e){var t=e.topic,n=e.seniority,c=Object(a.useState)([]),o=Object(b.a)(c,2),u=o[0],i=o[1],l=Object(a.useState)("loading"),s=Object(b.a)(l,2),m=s[0],d=s[1];return Object(a.useEffect)((function(){(function(e,t){return p.get("knowledgeblocks",{params:{title:e,level:null===t||void 0===t?void 0:t.toUpperCase()}}).then((function(e){return e.data.knowledgeBlockList}))})(t,n).then(i).then((function(){return d("success")})).catch((function(e){console.error(e),d("error")}))}),[t,n]),r.a.createElement(G,null,r.a.createElement(j,{loadingState:m},u.length>0?u.map((function(e){return r.a.createElement(V,{key:e.id,result:e})})):r.a.createElement("p",null,r.a.createElement("em",null,"No results found :("))))}));function W(){var e=Object(u.a)(["\n  margin-left: 10px;\n"]);return W=function(){return e},e}function X(){var e=Object(u.a)(["\n  margin-left: 8px;\n  max-width: 60%;\n"]);return X=function(){return e},e}function Y(){var e=Object(u.a)(["\n  display: block;\n  margin-bottom: 5px;\n"]);return Y=function(){return e},e}function Z(){var e=Object(u.a)(["\n  border-bottom: 1px solid black;\n  padding-bottom: 15px;\n  margin-bottom: 15px;\n"]);return Z=function(){return e},e}var $=s.b.form(Z()),ee=s.b.label(Y()),te=s.b.select(X()),ne=s.b.button(W()),ae=function(e){return function(t){e(t.currentTarget.value)}},re=function(){var e=Object(l.g)(),t=d.a.parse(e.location.search.slice(1)),n=t.topic,c=t.seniority,o=Object(a.useState)([]),u=Object(b.a)(o,2),i=u[0],s=u[1],m=Object(a.useState)(null!==n&&void 0!==n?n:""),f=Object(b.a)(m,2),v=f[0],O=f[1],g=Object(a.useState)("loading"),k=Object(b.a)(g,2),y=k[0],x=k[1],S=Object(a.useState)(null!==c&&void 0!==c?c:""),w=Object(b.a)(S,2),C=w[0],L=w[1];Object(a.useEffect)((function(){p.get("topics").then((function(e){return e.data})).then(h).then(s).then((function(){return x("success")})).catch((function(e){console.error(e),x("error")}))}),[]);var I=Object(a.useCallback)((function(t){t.preventDefault();var n={topic:v,seniority:C};Object.entries(n).forEach((function(e){var t=Object(b.a)(e,2),a=t[0];t[1]||delete n[a]})),e.push("/search?".concat(d.a.stringify(n)))}),[e,C,v]),P=Object(a.useCallback)((function(){L(""),O("")}),[]);return r.a.createElement(r.a.Fragment,null,r.a.createElement("h1",null,"Architect Notes"),r.a.createElement(j,{loadingState:y},r.a.createElement($,{onSubmit:I},r.a.createElement(ee,null,"Select topic:",r.a.createElement(te,{value:v,onChange:ae(O)},r.a.createElement("option",{value:"",disabled:!0},"Topic"),i.map((function(e){return r.a.createElement("option",{key:e.id,value:e.title},"".concat("--".repeat(e.depth)," ").concat(e.title))})))),r.a.createElement(ee,null,"Select seniority level:",r.a.createElement(te,{value:C,onChange:ae(L)},r.a.createElement("option",{value:"",disabled:!0},"Seniority"),E.map((function(e){return r.a.createElement("option",{key:e,value:e},e)})))),r.a.createElement("div",null,r.a.createElement("button",{disabled:!v},"Search"),r.a.createElement(l.b,{path:"/search"},r.a.createElement(ne,{onClick:P},"Clear"))))),r.a.createElement(l.d,null,r.a.createElement(l.b,{path:"/search"},n?r.a.createElement(Q,{topic:n,seniority:c}):r.a.createElement(l.a,{to:"/"})),r.a.createElement(l.b,{path:"/knowledge-block"},r.a.createElement(K,null)),r.a.createElement(l.b,{path:"*"},r.a.createElement(l.a,{to:"/"}))))};function ce(){var e=Object(u.a)(["\n  body {\n    font-family: Montserrat, 'sans-serif';\n  }\n"]);return ce=function(){return e},e}var oe=Object(s.a)(ce());o.a.render(r.a.createElement((function(){return r.a.createElement(i.a,null,r.a.createElement(oe,null),r.a.createElement(l.d,null,r.a.createElement(l.b,{path:"/",component:re})))}),null),document.getElementById("root"))},65:function(e,t,n){e.exports=n(100)}},[[65,1,2]]]);
//# sourceMappingURL=main.b004bb3f.chunk.js.map