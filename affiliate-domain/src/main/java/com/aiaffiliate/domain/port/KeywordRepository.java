package com.aiaffiliate.domain.port;

import com.aiaffiliate.domain.model.*;
import java.util.*;

/** 关键词持久化端口。 */
public interface KeywordRepository {
    Keyword save(Keyword value); Keyword update(Keyword value); Optional<Keyword> findById(KeywordId id);
    List<Keyword> findAll(); List<Keyword> findByStatus(KeywordStatus status); PageResult<Keyword> findPage(PageRequest page);
    List<Keyword> findByCategoryAndIntent(String category, KeywordIntent intent); void archive(KeywordId id);
}
