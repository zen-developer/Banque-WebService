/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package serv;

import db.Card;
import db.CardJpaController;
import java.util.Date;
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
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyBank_webservPU");
    CardJpaController cc = new CardJpaController(emf);
    EntityManager em = emf.createEntityManager();
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
            v.setMessage("Erreur : Carte introuvable");
            v.setValide(false);
            return v;
        }
        else
        {
            if(c.getDateExp().getTime()-System.currentTimeMillis()<=0)
            {
                v.setMessage("Erreur : Carte expirée");
                v.setValide(false);
                return v;
            }
            else
            {
                v.setMessage("Succés : Carte validée et enregistrement effectué");
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

    /**
     * Web service operation
     */
    @WebMethod(operationName = "validerCarte")
    public Validation validerCarte(@WebParam(name = "num_carte") String num_carte, @WebParam(name = "type_carte") String type_carte, @WebParam(name = "date_exp") Date date_exp, @WebParam(name = "nom_carte") String nom_carte, @WebParam(name = "cvv") int cvv) {
        //TODO write your implementation code here:
        Validation v=new Validation();
        
        Card c=null;
        System.out.println("NUM CARTE = "+num_carte+" | Type CARTE = "+type_carte);
        c=(Card)em.find(Card.class, num_carte);
        
        if(c==null)
        {
            v.setMessage("carte introuvable");
            v.setValide(false);
            return v;
        }
        else
        {
            System.out.println(c.getDateExp().getTime()+" VS "+date_exp.getTime());
            if (c.getTypeCarte().equalsIgnoreCase(type_carte) && c.getCvv() == cvv && c.getDateExp().getTime() == date_exp.getTime() && c.getNomCarte().equalsIgnoreCase(nom_carte)){
                if(c.getDateExp().getTime()-System.currentTimeMillis()<=0)
                {
                    v.setMessage("carte expirée");
                    v.setValide(false);
                    return v;
                }
                else{
                    v.setMessage("carte valide");
                    v.setValide(true);
                    return v;
                }
                    
            } 
            else
            {
                v.setMessage("carte invalide");
                v.setValide(false);
                return v;
            }
                
        }
    }
}
