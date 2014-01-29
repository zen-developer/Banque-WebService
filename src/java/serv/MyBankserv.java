/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package serv;

import db.Card;
import db.CardJpaController;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Validation;

/**
 *
 * @author Mariam_B
 */
@WebService(serviceName = "MyBankserv")
public class MyBankserv {
EntityManagerFactory emf = Persistence.createEntityManagerFactory("Bank_webservPU");
    CardJpaController cc = new CardJpaController(emf);
    EntityManager em =emf.createEntityManager();
    /**
     * This is a sample web service operation
     */
 
    @WebMethod(operationName = "valideCarte")
    public Validation valideCarte(int ind)
    {
       Validation v=new Validation();
        Card c=null;
        c=(Card)em.find(Card.class,ind);
        if(c==null)
        {
            v.setMessage("carte introuvable");
            v.setValide(false);
            return v;
        }
        else
        {
            if(c.getDateExp().getTime()-System.currentTimeMillis()<=0)
            {
                v.setMessage("carte expirée");
                v.setValide(false);
                return v;
            }
            else
            {
                v.setMessage("carte valide");
                v.setValide(true);
                return v;
            }
                
        }
    }
    
    
    @WebMethod(operationName = "verifSolde")
    public String verifSolde(@WebParam(name = "montant") float montant, @WebParam(name = "num") int num) throws Exception {
        //TODO write your implementation code here:
        String msg="";
        Card c =(Card)em.find(Card.class,num);
        float solde = c.getSolde();
        if(montant>solde)
         msg="La transaction ne peut pas être effectuée";
         else{
         msg="La transaction peut être effectuée";
         float res = solde-montant;
         c.setSolde(res);
         cc.edit(c);
        }
        return msg;
    }
}
