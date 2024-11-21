import { BankApi } from "../api/api.services";

export async function loginUser(dispatch, loginPayload) {

	const requestOptions = {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(loginPayload),
	};

	try {
		dispatch({ type: 'REQUEST_LOGIN' });
		try {
			let response = await BankApi.login(JSON.parse(requestOptions.body));
			if(response.statusCode === 400){
				alert(response.message);
				return;
			} 
			let data = response;
			
			if (data) {
				dispatch({ type: 'LOGIN_SUCCESS', payload: data });
				return data;
			}
			dispatch({ type: 'LOGIN_ERROR', error: data.errors[0] });
			console.log(data.errors[0]);
			return;
		} catch (error) {
			console.log(error);
		}



	} catch (error) {
		dispatch({ type: 'LOGIN_ERROR', error: error });
		console.log(error);
	}
}

export async function logout(dispatch) {
	dispatch({ type: 'LOGOUT' });
}
