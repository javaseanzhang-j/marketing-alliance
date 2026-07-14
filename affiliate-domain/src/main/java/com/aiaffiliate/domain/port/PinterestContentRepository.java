package com.aiaffiliate.domain.port;

import com.aiaffiliate.domain.model.*;
import java.util.*;

/** Pinterest 内容持久化端口。 */
public interface PinterestContentRepository {
    PinterestContent save(PinterestContent value); PinterestContent update(PinterestContent value); Optional<PinterestContent> findById(PinterestContentId id);
    List<PinterestContent> findAll(); List<PinterestContent> findByStatus(PinterestContentStatus status); PageResult<PinterestContent> findPage(PageRequest page);
    List<PinterestContent> findByOpportunityId(OpportunityId opportunityId); void archive(PinterestContentId id);
}
