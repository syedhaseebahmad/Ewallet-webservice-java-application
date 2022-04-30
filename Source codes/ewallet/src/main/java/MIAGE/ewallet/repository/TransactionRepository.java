/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIAGE.ewallet.repository;

import MIAGE.ewallet.models.SaveTransaction;
import static org.hibernate.loader.Loader.SELECT;
import static org.hibernate.sql.ast.Clause.FROM;
import static org.hibernate.sql.ast.Clause.LIMIT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Syed Haseeb
 */
public interface TransactionRepository extends JpaRepository<SaveTransaction,String>{
}
