import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { saveTokens } from "../Utils/Authentication";
import 'bootstrap/dist/css/bootstrap.css';
import './Login.css'; // Import the custom CSS
import SignUpForm from "../Components/SignUpForm";

function SignUp() {
    const [userRequest, setUserRequest] = useState({
        firstName: "",
        lastName: "",
        email: "",
        phoneNumber: "",
        username: "",
        password: "",
        userRole: "USER"
    });
    const navigate = useNavigate("");

    const handleSignUp = async (e) => {
        e.preventDefault(); // Prevent form submission
        const response = await fetch("http://localhost:8080/api/users/auth/createUser", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userRequest)
        });

        if (response.ok) {
            const data = await response.json();
            saveTokens(data.accessToken, data.refreshToken);
            console.log(data.message);
            navigate("/createAccount");
        }
        else {
            alert("Registration Failed. Please try again!");
        }
    };

    const handleLogin = () => {
        navigate("/login");
    };

    const handleChange = (e) => {
        setUserRequest({
            ...userRequest,
            [e.target.name]: e.target.value
        });
    };

    return (
        <SignUpForm
            handleSignUp={handleSignUp}
            handleLogin={handleLogin}
            handleChange={handleChange}
            userRequest={userRequest}
        />
    );
};

export default SignUp;