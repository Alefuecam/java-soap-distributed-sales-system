/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdb;

//import entidades.Envios;
import entidades.Envios;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
//import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
//import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alefu
 */
//@JMSDestinationDefinition(name = "java:app/jms/SolicitudEnvios", interfaceName = "javax.jms.Queue", resourceAdapter = "jmsra", destinationName = "SolicitudEnvios")
//@MessageDriven(activationConfig = {
//    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:app/jms/SolicitudEnvios")
//    ,
//        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
//})
@JMSDestinationDefinition(name = "java:app/jms/SolicitudEnvios", interfaceName = "javax.jms.Queue", resourceAdapter = "jmsra", destinationName = "SolicitudEnvios")
@MessageDriven(mappedName = "jms/SolicitudEnvios", activationConfig =  {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
    })

public class SolicitudEnvios implements MessageListener {
    @Resource
    private MessageDrivenContext mdc;
    @PersistenceContext(unitName = "WS_EnviosPU")
    private EntityManager em;
    
    public SolicitudEnvios() {
    }
    
    @Override
    public void onMessage(Message message) {
              ObjectMessage msg = null;
       
      try 
      {
        if (message instanceof ObjectMessage) {
            msg = (ObjectMessage) message;
            Envios envio = (Envios) msg.getObject();
            
            Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                    "Recibiendo solicitud de envio para:" + envio.getName() + ", para el pedido:" + envio.getIdCustomerOrder());

            
            save(envio);
        }
      } 
      catch (JMSException e) 
      {
        e.printStackTrace();
        mdc.setRollbackOnly();
      } 
      catch (Throwable te) 
      {
        te.printStackTrace();
      } 
    }

    public void save(Object object) {
        em.persist(object);
    }
    
}
