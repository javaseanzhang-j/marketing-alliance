package com.aiaffiliate.domain.port;

import com.aiaffiliate.domain.model.*;
import java.util.*;

/** Bridge Page 持久化端口。 */
public interface BridgePageRepository {
    BridgePage save(BridgePage value); BridgePage update(BridgePage value); Optional<BridgePage> findById(BridgePageId id);
    List<BridgePage> findAll(); List<BridgePage> findByStatus(BridgePageStatus status); PageResult<BridgePage> findPage(PageRequest page);
    List<BridgePage> findByOpportunityId(OpportunityId opportunityId); void archive(BridgePageId id);
}
