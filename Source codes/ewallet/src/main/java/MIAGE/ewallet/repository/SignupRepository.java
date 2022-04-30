    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIAGE.ewallet.repository;

import MIAGE.ewallet.models.User;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Sanjeewa Kulathunga
 */

public interface SignupRepository extends CrudRepository<User,String>{}
