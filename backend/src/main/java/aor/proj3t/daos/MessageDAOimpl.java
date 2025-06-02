package aor.proj3t.daos;

import aor.proj3t.entities.Message;
import jakarta.ejb.Stateless;

@Stateless
public class MessageDAOimpl extends AbstractDAO<Message, Long> implements MessageDAO {

}
