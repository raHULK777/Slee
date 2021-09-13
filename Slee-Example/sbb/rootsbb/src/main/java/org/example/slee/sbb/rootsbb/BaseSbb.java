package org.example.slee.sbb.rootsbb;

import com.opencloud.javax.sip.slee.OCSipActivityContextInterfaceFactory;
import com.opencloud.javax.sip.slee.OCSleeSipProvider;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.SipProvider;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.slee.*;
import javax.slee.facilities.Tracer;

public abstract class BaseSbb implements Sbb,SbbLocalObject {
    protected Tracer tracer;
    private OCSleeSipProvider sipSbbInterface;
    private OCSipActivityContextInterfaceFactory sipACIFactory;
    private SbbContext sbbContext;

    public void setSbbContext(SbbContext context) {
        tracer = context.getTracer(context.getSbb().getName());
        try {
            sbbContext=context;
            Context myEnv = (Context) new InitialContext().lookup("java:comp/env");
            String providerName ="java:comp/env/slee/resources/sipra/provider";
            String factoryName ="java:comp/env/slee/resources/sipra/acifactory";
            sipSbbInterface = (OCSleeSipProvider) myEnv.lookup(providerName);
            sipACIFactory = (OCSipActivityContextInterfaceFactory) myEnv.lookup(factoryName);
        }
        catch (NamingException e) {
            tracer.severe("Could not set SBB context", e);
        }
    }

    public SbbLocalObject getSbblocalObject() {
        return this.sbbContext.getSbbLocalObject();
    }

    public void unsetSbbContext() {

    }

    public void sbbCreate() throws CreateException {

    }

    public void sbbPostCreate() throws CreateException {

    }

    public void sbbActivate() {

    }

    public void sbbPassivate() {

    }

    public void sbbLoad() {

    }

    public void sbbStore() {

    }

    public void sbbRemove() {

    }

    public void sbbExceptionThrown(Exception e, Object o, ActivityContextInterface activityContextInterface) {

    }

    public void sbbRolledBack(RolledBackContext rolledBackContext) {

    }
    protected final OCSleeSipProvider getSleeSipProvider() { return sipSbbInterface; }
    protected final OCSipActivityContextInterfaceFactory getSipACIFactory() { return sipACIFactory; }
    protected final SipProvider getSipProvider() { return sipSbbInterface; }
    protected final AddressFactory getSipAddressFactory() {
        return sipSbbInterface.getAddressFactory();
    }
    protected final HeaderFactory getSipHeaderFactory() { return sipSbbInterface.getHeaderFactory(); }
    protected final MessageFactory getSipMessageFactory() {
        return sipSbbInterface.getMessageFactory();
    }

    public boolean isIdentical(SbbLocalObject sbbLocalObject) throws TransactionRequiredLocalException, SLEEException {
        return false;
    }

    public void setSbbPriority(byte b) throws TransactionRequiredLocalException, NoSuchObjectLocalException, SLEEException {

    }

    public byte getSbbPriority() throws TransactionRequiredLocalException, NoSuchObjectLocalException, SLEEException {
        return 0;
    }

    public void remove() throws TransactionRequiredLocalException, TransactionRolledbackLocalException, SLEEException {

    }
}
