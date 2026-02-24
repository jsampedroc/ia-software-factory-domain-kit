package com.application.domain.port;

import com.application.domain.model.ConsultingRoom;
import com.application.domain.valueobject.ConsultingRoomId;
import com.application.domain.shared.EntityRepository;

public interface ConsultingRoomRepositoryPort extends EntityRepository<ConsultingRoom, ConsultingRoomId> {
}