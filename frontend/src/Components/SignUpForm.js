import React from 'react';

function SignUpForm({ handleSignUp, handleLogin, handleChange, userRequest }) {
    return (
        <div>
            <section class="background-radial-gradient overflow-hidden">
                <div class="container px-4 py-5 px-md-5 text-center text-lg-start my-5">
                    <div class="row gx-lg-5 align-items-center mb-5">
                        <div class="col-lg-6 mb-5 mb-lg-0" style={{ zIndex: 10 }}>
                            <h1 class="my-5 display-5 fw-bold ls-tight" style={{ color: "hsl(218, 81%, 95%)" }}>
                                The best offer <br />
                                <span style={{ color: "hsl(218, 81%, 75%)" }}>for your business</span>
                            </h1>
                            <p class="mb-4 opacity-70" style={{ color: "hsl(218, 81%, 85%)" }}>
                                Lorem ipsum dolor, sit amet consectetur adipisicing elit.
                                Temporibus, expedita iusto veniam atque, magni tempora mollitia
                                dolorum consequatur nulla, neque debitis eos reprehenderit quasi
                                ab ipsum nisi dolorem modi. Quos?
                            </p>
                        </div>
                        <div class="col-lg-6 mb-5 mb-lg-0 position-relative">
                            <div id="radius-shape-1" class="position-absolute rounded-circle shadow-5-strong"></div>
                            <div id="radius-shape-2" class="position-absolute shadow-5-strong"></div>
                            <div class="card bg-glass">
                                <div class="card-body px-4 py-5 px-md-5">
                                    <form onSubmit={handleSignUp}>
                                        <div class="row">
                                            <div class="col-md-6 mb-4">
                                                <div data-mdb-input-init class="form-outline">
                                                    <input type="text" id="form3Example1" class="form-control" placeholder="First Name" name="firstName" value={userRequest.firstName} onChange={handleChange} />
                                                </div>
                                            </div>
                                            <div class="col-md-6 mb-4">
                                                <div data-mdb-input-init class="form-outline">
                                                    <input type="text" id="form3Example2" class="form-control"  placeholder="Last Name" name="lastName" value={userRequest.lastName} onChange={handleChange} />
                                                </div>
                                            </div>
                                        </div>                    
                                        <div class="row">
                                            <div class="col-md-6 mb-4">
                                                <div data-mdb-input-init class="form-outline">
                                                    <input type="text" id="form3Example1" class="form-control"  placeholder="Email" name="email" value={userRequest.email} onChange={handleChange}/>
                                                </div>
                                            </div>
                                            <div class="col-md-6 mb-4">
                                                <div data-mdb-input-init class="form-outline">
                                                    <input type="text" id="form3Example2" class="form-control"  placeholder="Phone Number" name="phoneNumber" value={userRequest.phoneNumber} onChange={handleChange} />
                                                </div>
                                            </div>
                                        </div>                    
                                        <div class="row">
                                            <div class="col-md-6 mb-4">
                                                <div data-mdb-input-init class="form-outline">
                                                    <input type="text" id="form3Example1" class="form-control"  placeholder="Userame" name="username" value={userRequest.username} onChange={handleChange}/>
                                                </div>
                                            </div>
                                            <div class="col-md-6 mb-4">
                                                <div data-mdb-input-init class="form-outline">
                                                    <input type="password" id="form3Example2" class="form-control"  placeholder="Password" name="password" value={userRequest.password} onChange={handleChange} />
                                                </div>
                                            </div>
                                        </div>                    
                                        <button type="submit" data-mdb-button-init data-mdb-ripple-init class="btn btn-primary btn-block mb-4 btn-space">
                                            Sign up
                                        </button>
                                        <button onClick={handleLogin} data-mdb-button-init data-mdb-ripple-init class="btn btn-primary btn-block mb-4">
                                            Login
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    );
};


export default SignUpForm;