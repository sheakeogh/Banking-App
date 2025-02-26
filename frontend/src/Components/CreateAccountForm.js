import React from 'react'

function CreateAccountForm({ handleCreateAccount, handleChange, accountRequest }) {
    return (
        <div>
            <section className="background-radial-gradient overflow-hidden">
                <div className="container px-4 py-5 px-md-5 text-center text-lg-start my-5">
                    <div className="row gx-lg-5 align-items-center mb-5">
                        <div className="col-lg-6 mb-5 mb-lg-0" style={{ zIndex: 10 }}>
                            <h1 className="my-5 display-5 fw-bold ls-tight" style={{ color: 'hsl(218, 81%, 95%)' }}>
                                Create an Account <br />
                            </h1>
                            <p className="mb-4 opacity-70" style={{ color: 'hsl(218, 81%, 85%)' }}>
                                Lorem ipsum dolor, sit amet consectetur adipisicing elit.
                                Temporibus, expedita iusto veniam atque, magni tempora mollitia
                                dolorum consequatur nulla, neque debitis eos reprehenderit quasi
                                ab ipsum nisi dolorem modi. Quos?
                            </p>
                        </div>        
                        <div className="col-lg-6 mb-5 mb-lg-0 position-relative">
                            <div id="radius-shape-1" className="position-absolute rounded-circle shadow-5-strong"></div>
                            <div id="radius-shape-2" className="position-absolute shadow-5-strong"></div>        
                            <div className="card bg-glass">
                                <div className="card-body px-4 py-5 px-md-5">
                                    <form onSubmit={handleCreateAccount}>
                                        <div data-mdb-input-init className="form-outline mb-4">
                                        <select id="form3Example3" className="form-control" name="accountType" value={accountRequest.accountType} onChange={handleChange}>
                                                <option value="">Select Account Type</option>
                                                <option value="CURRENT">Current</option>
                                                <option value="SAVINGS">Savings</option>
                                            </select>                                        </div>
                                        <button type="submit" data-mdb-button-init data-mdb-ripple-init className="btn btn-primary btn-block mb-4 btn-space">
                                            Create
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

export default CreateAccountForm;