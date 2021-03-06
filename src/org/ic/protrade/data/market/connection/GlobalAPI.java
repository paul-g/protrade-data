package org.ic.protrade.data.market.connection;

import org.ic.protrade.data.exceptions.ViewProfileException;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.APIRequestHeader;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.APIResponseHeader;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.EventType;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.GetActiveEventTypes;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.GetEventTypesReq;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.GetEventTypesResp;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.GetEvents;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.GetEventsErrorEnum;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.GetEventsReq;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.GetEventsResp;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.Login;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.LoginErrorEnum;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.LoginReq;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.LoginResp;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.Logout;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.LogoutErrorEnum;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.LogoutReq;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.LogoutResp;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.ViewProfile;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.ViewProfileErrorEnum;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.ViewProfileReq;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.ViewProfileResp;

public class GlobalAPI {

	private static BFGlobalServiceStub stub;

	// Lasy load the Global service stub generated by Apache Axis.
	// This stub is used to make all requests to the Betfair Global API
	// The global API is generally used for account management features
	private static BFGlobalServiceStub getStub() throws Exception {
		if (stub == null) {
			stub = new BFGlobalServiceStub(
					"https://api.betfair.com/global/v3/BFGlobalService");

			// You may set up the connection parameters of the stub here if
			// necessary
			// For example: Wait 20 seconds for a response from the API
			stub._getServiceClient().getOptions()
					.setTimeOutInMilliSeconds(20 * 1000);
			stub._getServiceClient()
					.getOptions()
					.setProperty(
							org.apache.axis2.transport.http.HTTPConstants.MC_ACCEPT_GZIP,
							"true");
			stub._getServiceClient()
					.getOptions()
					.setProperty(
							org.apache.axis2.transport.http.HTTPConstants.MC_GZIP_RESPONSE,
							"true");
		}
		return stub;
	}

	// Get the request header to add to the request
	private static APIRequestHeader getHeader(APIContext context) {
		APIRequestHeader header = new APIRequestHeader();
		// The header must have the session token attached.
		header.setSessionToken(context.getToken());
		return header;
	}

	// Save the data from the request header into the context
	private static void setHeaderDataToContext(APIContext context,
			APIResponseHeader header) {
		context.setToken(header.getSessionToken()); // May be updated in each
													// call.
		context.setLastCall(header.getTimestamp().getTime());
	}

	// Fire a Web services login request
	public static void login(APIContext context, String username,
			String password) throws Exception {
		// Create a login request object
		LoginReq request = new LoginReq();

		// Set the parameters you wish to pass the the API
		request.setUsername(username);
		request.setPassword(password);
		request.setProductId(82); // 82 is the standard Free Access API
		request.setIpAddress(""); // Does not need to be set, but may not be
									// null so use an empty string.

		// Create the Login message and attach the request to it.
		Login msg = new Login();
		msg.setRequest(request);

		// Send the request to the Betfair Service.
		LoginResp resp = getStub().login(msg).getResult();

		// Check the response code, and throw and exception if the call failed
		if (resp.getErrorCode() != LoginErrorEnum.OK) {
			throw new IllegalArgumentException("Failed to log in: "
					+ resp.getErrorCode() + " Minor Error:"
					+ resp.getMinorErrorCode() + " Header Error:"
					+ resp.getHeader().getErrorCode());
		}

		// Transfer the response data back to the API context
		setHeaderDataToContext(context, resp.getHeader());
	}

	// Get the active event types within the system (on both exchanges)
	public static EventType[] getActiveEventTypes(APIContext context)
			throws Exception {
		// Create a request object
		GetEventTypesReq request = new GetEventTypesReq();
		request.setHeader(getHeader(context));

		// Create the GetActiveEventTypes message and attach the request to it.
		GetActiveEventTypes msg = new GetActiveEventTypes();
		msg.setRequest(request);

		// Send the request to the Betfair Service.
		GetEventTypesResp resp = getStub().getActiveEventTypes(msg).getResult();

		// Check the response code, and throw and exception if the call failed
		if (resp.getErrorCode() != GetEventsErrorEnum.OK) {
			throw new IllegalArgumentException(
					"Failed to retrieve active event types: "
							+ resp.getErrorCode() + " Minor Error:"
							+ resp.getMinorErrorCode() + " Header Error:"
							+ resp.getHeader().getErrorCode());
		}

		// Transfer the response data back to the API context
		setHeaderDataToContext(context, resp.getHeader());

		return resp.getEventTypeItems().getEventType();
	}

	// Get the markets available for the event specified
	public static GetEventsResp getEvents(APIContext context, int eventId)
			throws Exception {
		// Create a request object
		GetEventsReq request = new GetEventsReq();
		request.setHeader(getHeader(context));

		// Set the parameter
		request.setEventParentId(eventId);

		// Create the GetActiveEventTypes message and attach the request to it.
		GetEvents msg = new GetEvents();
		msg.setRequest(request);

		// Send the request to the Betfair Service.
		GetEventsResp resp = getStub().getEvents(msg).getResult();

		// Check the response code, and throw and exception if the call failed
		if (resp.getErrorCode() != GetEventsErrorEnum.OK) {
			throw new IllegalArgumentException("Failed to retrieve events: "
					+ resp.getErrorCode() + " Minor Error:"
					+ resp.getMinorErrorCode() + " Header Error:"
					+ resp.getHeader().getErrorCode());
		}

		// Transfer the response data back to the API context
		setHeaderDataToContext(context, resp.getHeader());

		return resp;
	}

	// Fire a Web services logout request
	public static void logout(APIContext context) throws Exception {
		// Create a request object
		LogoutReq request = new LogoutReq();
		request.setHeader(getHeader(context));

		// Create the Logout message and attach the request to it.
		Logout msg = new Logout();
		msg.setRequest(request);

		// Send the request to the Betfair Service.
		LogoutResp resp = getStub().logout(msg).getResult();

		// Check the response code, and throw and exception if the call failed
		if (resp.getErrorCode() != LogoutErrorEnum.OK) {
			throw new IllegalArgumentException("Failed to log out: "
					+ resp.getErrorCode() + " Minor Error:"
					+ resp.getMinorErrorCode() + " Header Error:"
					+ resp.getHeader().getErrorCode());
		}

		setHeaderDataToContext(context, resp.getHeader());
	}

	// Return Profile info
	public static ViewProfileResp getProfile(APIContext context)
			throws ViewProfileException {
		// Create the request
		ViewProfileReq request = new ViewProfileReq();
		request.setHeader(getHeader(context));
		// Create the profile
		ViewProfile profile = new ViewProfile();
		profile.setRequest(request);
		// post the query and get the response
		ViewProfileResp resp;
		try {
			resp = getStub().viewProfile(profile).getResult();
		} catch (Exception e) {
			throw new ViewProfileException(e.getMessage());
		}
		// check if request successful
		if (resp.getErrorCode() != ViewProfileErrorEnum.OK)
			throw new ViewProfileException(resp.getErrorCode().toString());
		setHeaderDataToContext(context, resp.getHeader());
		return resp;
	}

}
