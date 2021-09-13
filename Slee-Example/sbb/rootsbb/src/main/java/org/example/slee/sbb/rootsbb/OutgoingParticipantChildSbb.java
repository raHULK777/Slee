package org.example.slee.sbb.rootsbb;

import net.java.slee.resource.sip.DialogActivity;
import org.example.slee.sbb.local.OutgoingPartcipantLocalInterface;
import org.example.slee.sbb.local.RootSbbLocalInterface;
import org.example.slee.sbb.utility.SipUtils;

import javax.sip.*;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;

public abstract class OutgoingParticipantChildSbb extends BaseSbb implements OutgoingPartcipantLocalInterface {
    @Override
    public void setSbbContext(SbbContext context) {
        super.setSbbContext(context);
    }

    public void init(RequestEvent event,DialogActivity outgoingDialog, SbbLocalObject parent) {
        tracer.info("outgoingSBB received invite"+event.getRequest().toString());
        ServerTransaction st=event.getServerTransaction();
        ActivityContextInterface outgoingDialogACI;
        try {
            outgoingDialogACI=getSipACIFactory().getActivityContextInterface(outgoingDialog);
            outgoingDialogACI.attach(getSbblocalObject());
            forwardRequest(event, outgoingDialog);
            setParent((RootSbbLocalInterface) parent);
            setOutgoingParticipantACI(outgoingDialogACI);
        } catch (Exception e) {
            tracer.info("Exception",e);
        }

    }

    public void forwardRequest(RequestEvent event,DialogActivity outgoingDialog) {
        tracer.info("forwarding request to UAS        "+event.getRequest());
           try {
               Request outgoingRequest=outgoingDialog.createRequest(event.getRequest());
               SipURI outURI=SipUtils.getSipURIFromAOR((SipURI) event.getRequest().getRequestURI());
               outgoingRequest.setRequestURI(outURI);
               ClientTransaction ct=outgoingDialog.sendRequest(outgoingRequest);
               outgoingDialog.associateServerTransaction(ct,event.getServerTransaction());
               setOutURI(outURI.toString());

           } catch (Exception e) {
               tracer.info("Exception", e);
           }
    }

    public void on1xxResponse(ResponseEvent event, ActivityContextInterface aci) {
        Response response = event.getResponse();
        tracer.info("got 1XX response from UAS"+response);
        processReliableResponse(event, aci);
    }

    public void on2xxResponse(ResponseEvent event, ActivityContextInterface aci) {
        Response response=event.getResponse();
        tracer.info("got 2XX response from UAS"+response);
        ViaHeader viaResponse= (ViaHeader) response.getHeader(ViaHeader.NAME);
        if(getBranch()!=null && getBranch().equals(viaResponse.getBranch())){
            tracer.info("got 200 OK for bye and no need to forward it to UAC");
            getOutgoingParticipantACI().detach(getSbblocalObject());
            setOutgoingParticipantACI(null);
        }
        else
            processReliableResponse(event, aci);
    }

    public void on3xxResponse(ResponseEvent event, ActivityContextInterface aci) {
        processReliableResponse(event, aci);
    }

    public void on4xxResponse(ResponseEvent event, ActivityContextInterface aci) {
        processReliableResponse(event, aci);
    }

    public void on5xxResponse(ResponseEvent event, ActivityContextInterface aci) {
        processReliableResponse(event, aci);
    }

    public void on6xxResponse(ResponseEvent event, ActivityContextInterface aci) {
        processReliableResponse(event, aci);
    }


    public void processReliableResponse(ResponseEvent event,ActivityContextInterface aci) {
        DialogActivity outgoingDialog= (DialogActivity) getOutgoingParticipantACI().getActivity();
        ServerTransaction st=outgoingDialog.getAssociatedServerTransaction(event.getClientTransaction());
        getParent().forwardResponseToLeg1(event,st);
    }

    public void sendRequestToLeg2(RequestEvent event ) {
        if(event.getRequest().getMethod().equals(Request.ACK)) {
            tracer.info("ack is calling");
            forwardAck(event, (DialogActivity) getOutgoingParticipantACI().getActivity());
        }
        else if(event.getRequest().getMethod().equals(Request.BYE)) {
            tracer.info("bye is calling");
            forwardBye(event, (DialogActivity) getOutgoingParticipantACI().getActivity());
        }
    }

    public void forwardAck(RequestEvent event,DialogActivity outgoingDialog) {
        tracer.info("forwarding Ack to UAS   "+event.getRequest().toString());
        try {
            Request outgoingRequest=outgoingDialog.createRequest(event.getRequest());
            tracer.info("ACK  "+outgoingRequest);
            tracer.info("remote party"+outgoingDialog.getRemoteParty());
            tracer.info("Local Party"+outgoingDialog.getLocalParty());
//            tracer.info("remote port"+outgoingDialog.get);
            Address address=getSipAddressFactory().createAddress(getOutURI());
            outgoingRequest.setRequestURI(address.getURI());
            tracer.info("set URI"+outgoingRequest);
//            outgoingDialog.getFirstTransaction().
            outgoingDialog.sendAck(outgoingRequest);
            tracer.info("sent ACK to UAS successfully");
        } catch (Exception e) {
            tracer.info("unable to send Ack"+e);
        }
    }

    public void forwardBye(RequestEvent event,DialogActivity outgoingDialog) {
        try {
            Request outgoingRequest=outgoingDialog.createRequest(event.getRequest());
            outgoingDialog.sendRequest(outgoingRequest);
            tracer.info("sending BYE successfully  "+outgoingRequest);
            ViaHeader via= (ViaHeader) outgoingRequest.getHeader(ViaHeader.NAME);
            setBranch(via.getBranch());
        } catch (SipException e) {
            System.out.println("Exception"+e);
        }
    }


    public abstract RootSbbLocalInterface getParent();
    public abstract void setParent(RootSbbLocalInterface parent);

    public abstract ActivityContextInterface getOutgoingParticipantACI();
    public abstract void setOutgoingParticipantACI(ActivityContextInterface outgoingParticipantACI);

    public abstract String getOutURI();
    public abstract void setOutURI(String outURI);

    public abstract String getBranch();
    public abstract void setBranch(String branch);

}
