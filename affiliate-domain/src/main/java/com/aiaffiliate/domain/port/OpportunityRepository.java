package com.aiaffiliate.domain.port;

import com.aiaffiliate.domain.model.*;
import java.util.*;

/** 机会持久化端口。 */
public interface OpportunityRepository {
    Opportunity save(Opportunity value); Opportunity update(Opportunity value); Optional<Opportunity> findById(OpportunityId id);
    List<Opportunity> findAll(); List<Opportunity> findByStatus(OpportunityStatus status); PageResult<Opportunity> findPage(PageRequest page);
    List<Opportunity> findByPriorityAndChannel(OpportunityPriority priority, MarketingChannel channel); void archive(OpportunityId id);
}
