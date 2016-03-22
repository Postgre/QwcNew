package com.gservfocus.qwc.net.util;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.gservfocus.qwc.QwcApplication;
import com.gservfocus.qwc.net.WSError;

public class WebServicesUtil {
	
	
	private static Object detail;

	public static String doWebServicesRequest(SoapObject rpc,String url) throws WSError{
		//���粻����ʱ��return�������
		if(!NetAvailable.isNetAvailable(QwcApplication.getInstance())){
			return "{\"result\":\"-1\"}";
		}
		// ���ɵ���WebService������SOAP������Ϣ
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		// ����HttpTransportsSE����
		HttpTransportSE ht = new HttpTransportSE(url);
		ht.debug = true ;
		try {
			ht.call(rpc.getNamespace()+"/"+rpc.getName(), envelope);
			detail = envelope.getResponse();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			WSError wsError = new WSError();
			wsError.setMessage(e.getLocalizedMessage());
			throw wsError;
		} 
		return detail.toString();
	}

}
