/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIAGE.ewallet.repository;

import MIAGE.ewallet.models.Account;
import MIAGE.ewallet.models.SaveTransaction;
import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Syed Haseeb
 */
public interface AccountRepository extends CrudRepository<Account,String>{}
