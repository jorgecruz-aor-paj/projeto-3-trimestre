package aor.proj3t.daos.impl;

import aor.proj3t.daos.interfaces.ResetPasswordDAO;
import aor.proj3t.entities.ResetPassword;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

@Stateless
public class ResetPasswordDAOimpl extends AbstractDAO<ResetPassword, Long> implements ResetPasswordDAO {

    @Override
    protected Class<ResetPassword> getEntityClass() {
        return ResetPassword.class;
    }

}
