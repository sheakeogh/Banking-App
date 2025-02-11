import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { saveTokens } from "../Utils/Authentication";
import 'bootstrap/dist/css/bootstrap.css';
import './Login.css';
import LoginForm from "../Components/LoginForm";

function Login() {
    const [loginRequest, setLoginRequest] = useState({
        username: "",
        password: ""
    });
    const navigate = useNavigate("");

    const handleLogin = async (e) => {
        e.preventDefault();
        const response = await fetch("http://localhost:8080/api/users/auth/loginUser", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(loginRequest)
        });

        if (response.ok) {
            const data = await response.json();
            saveTokens(data.accessToken, data.refreshToken);
            console.log(data.message);
            navigate("/home");
        }
        else {
            alert("Registration Failed. Please try again!");
        }
    };

    const handleSignUp = () => {
        navigate("/signUp");
    };

    const handleChange = (e) => {
        setLoginRequest({
            ...loginRequest,
            [e.target.name]: e.target.value
        });
    };

    return (
        <LoginForm
            handleLogin={handleLogin}
            handleChange={handleChange}
            handleSignUp={handleSignUp}
            loginRequest={loginRequest}
        />
    );
};

export default Login;