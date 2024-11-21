import './App.css';
import "primereact/resources/themes/lara-light-cyan/theme.css";
import { useContext, useEffect,useReducer } from 'react';
import { BrowserRouter as Router, Routes } from "react-router-dom";
import Default from './pages/default';
import Banking from './pages/banking';
import { BankApi } from './api/api.services';
import { AuthProvider } from './Context';
import { useAuthState,useAuthDispatch } from './Context/context';
import { Master } from './pages/master';


function App() {



  return (
    
    <AuthProvider>
        <Master/>
    </AuthProvider>
  );
}

export default App;
