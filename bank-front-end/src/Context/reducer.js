import React, { useState, useReducer } from 'react';


export const initialState = {
	id: null,
	username: null,
	accounts:null
};

export const AuthReducer = (initialState, action) => {
	switch (action.type) {
		case 'REQUEST_LOGIN':
			return {
				...initialState,
				loading: true,
			};
		case 'LOGIN_SUCCESS':
			return {				
				...initialState,
				id: action.payload.id,
				accounts: action.payload.accountDTOS,
				username: action.payload.username,
			};
		case 'LOGOUT':
			return {
				...initialState,
				id: null,
				username: '',
			};

		case 'LOGIN_ERROR':
			return {
				...initialState,
				loading: false,
				errorMessage: action.error,
			};

		default:
			throw new Error(`Unhandled action type: ${action.type}`);
	}
};
