package aor.proj3t.daos.impl;

import aor.proj3t.daos.interfaces.MessageDAO;
import aor.proj3t.entities.Message;
import jakarta.ejb.Stateless;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MessageDAOimpl extends AbstractDAO<Message, Long> implements MessageDAO {


    @Override
    public List<Message> findBySenderId(Long senderId) {
        return List.of();
    }

    @Override
    public List<Message> findByRecipientId(Long recipientId) {
        return List.of();
    }

}
