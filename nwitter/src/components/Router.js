import React from "react";
import {HashRouter as Router, Redirect, Route, Switch} from "react-router-dom";
import Auth from "routes/Auth";
import Home from "routes/Home";
import Profile from "routes/Profile";
import Navigation from "./Navigation";

const AppRouter = ({isLoggedIn, userObj}) => {
    return (
        <Router>
            {isLoggedIn && <Navigation />}
            <Switch>
                {isLoggedIn ? (
                    <>
                        <Route exact path="/">
                            <Home userObj={userObj}/>
                        </Route>
                        <Route exact path="/profile">
                            <Profile />
                        </Route>
                        <Redirect from="*" to="/" />
                    </>
                ) : (
                    <Route exact path="/">
                        <Auth />
                    </Route>
                )}
            </Switch>
        </Router>
    );
};
export default AppRouter