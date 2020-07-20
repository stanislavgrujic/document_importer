package com.stanislavgrujic.documentimporter.repository;

import com.stanislavgrujic.documentimporter.model.Vote;
import com.stanislavgrujic.documentimporter.model.VoteKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, VoteKey> {
}
