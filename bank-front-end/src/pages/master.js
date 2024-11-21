import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { useAuthState,useAuthDispatch } from '../Context/index';
import Default from "./default";
import Banking from "./banking";
export function Master(){

    const userDetails  = useAuthState();

    return(
        <Router>
        {
            userDetails.id === null ?
        <>
          <Routes>
            <Route path='/' Component={Default} />
          </Routes>
        </>
        :
        <>
          <Routes>
            <Route path='/' Component={Banking} />
          </Routes>
        </>
        
        }
      </Router>
    )
    
}