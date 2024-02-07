

package com.infobip.sms;

import com.infobip.ApiClient;
import com.infobip.ApiException;
import com.infobip.ApiKey;
import com.infobip.BaseUrl;
import com.infobip.api.SmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsTextualMessage;

import java.util.Collections;

public class SendSMS {

    // TODO: 04/02/2024  Fill out All APIS Information to Send SMS
    private static final String BASE_URL = "https://e1dlgq.api.infobip.com";
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String RECIPIENT = "YOUR REGISTER NUMBER";

    public static void send(String message){
        // Create the API client and the Send SMS API instances.
        ApiClient apiClient = ApiClient.forApiKey(ApiKey.from(API_KEY))
                .withBaseUrl(BaseUrl.from(BASE_URL))
                .build();
        SmsApi sendSmsApi = new SmsApi(apiClient);

        // Create a message to send.
        SmsTextualMessage smsMessage = new SmsTextualMessage()
                .addDestinationsItem(new SmsDestination().to(RECIPIENT))
                .text(message);

        // Create a send message request.
        SmsAdvancedTextualRequest smsMessageRequest = new SmsAdvancedTextualRequest()
                .messages(Collections.singletonList(smsMessage));

        try {
            // Send the message.
            com.infobip.model.SmsResponse smsResponse = sendSmsApi.sendSmsMessage(smsMessageRequest).execute();
            System.out.println("Response body: " + smsResponse);

            // Get delivery reports. It may take a few seconds to show the above-sent message.
            com.infobip.model.SmsDeliveryResult reportsResponse = sendSmsApi.getOutboundSmsMessageDeliveryReports().execute();
            System.out.println(reportsResponse.getResults());
        } catch (ApiException e) {
            System.out.println("HTTP status code: " + e.responseStatusCode());
            System.out.println("Response body: " + e.rawResponseBody());
        }
    }

    public static void main(String[] args) {
send("hi");
    }
}
