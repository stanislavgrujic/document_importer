package com.stanislavgrujic.documentimporter.service;

import com.stanislavgrujic.documentimporter.model.Paragraph;
import com.stanislavgrujic.documentimporter.model.User;
import com.stanislavgrujic.documentimporter.model.Vote;
import com.stanislavgrujic.documentimporter.model.VoteKey;
import com.stanislavgrujic.documentimporter.repository.ParagraphRepository;
import com.stanislavgrujic.documentimporter.repository.VoteRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ParagraphService {

  @Autowired
  private ParagraphRepository repository;

  @Autowired
  private VoteRepository voteRepository;

  public List<Paragraph> getTopics() {
    return repository.findByTitleIsNotNull();
  }

  public List<Paragraph> findKnowledgeBlocks(String title) {
    if (StringUtils.isEmpty(title)) {
      return repository.findAll();
    }
    return repository.findByTitleContaining(title);
  }

  public Paragraph findKnowledgeBlockById(long id) {
    return repository.getOne(id);
  }

  public long create(Paragraph paragraph, Long parentId) {
    Paragraph parent = new Paragraph();
    parent.setId(parentId);
    paragraph.setParent(parent);
    repository.save(paragraph);
    return paragraph.getId();
  }

  public void update(Paragraph paragraph) {
    repository.save(paragraph);
  }

  public void upVote(long id, User votedBy) {
    vote(id, votedBy, 1);
  }

  public void downVote(long id, User votedBy) {
    vote(id, votedBy, -1);
  }

  private void vote(long id, User votedBy, int voteResult) {
    Optional<Paragraph> paragraph = repository.findById(id);
    if (!paragraph.isPresent()) {
      throw new EntityNotFoundException("Knowledge block does not exist");
    }
    Paragraph voted = paragraph.get();

    User user = new User();
    user.setId(votedBy.getId());

    Vote vote = new Vote();
    vote.setId(new VoteKey(votedBy.getId(), id));
    vote.setVote(voteResult);
    voted.getVotes().add(vote);

    voteRepository.save(vote);
  }
}
