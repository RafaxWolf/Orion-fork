
const Register = () => {


    return (
        <div className="containter mt-5">
            <div className="row justify-content-center">
                <div className="col-md-6">
                    <div className="card">

                        <div className="card-header">
                            <h1>Orion</h1>
                        </div>

                        <div className="card-body">
                            <h2>Register</h2>

                            <div className="mb-3">
                                <label className="form-label">Username</label>
                                <input type="text" 
                                       className="form-control"
                                       required
                                />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Email</label>
                                <input type="text" 
                                       className="form-control"
                                       required
                                />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Contraseña</label>
                                <input type="text" 
                                       className="form-control"
                                       required
                                />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Confirmar Contraseña</label>
                                <input type="text" 
                                       className="form-control"
                                       required
                                />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Biografia</label>
                                <textarea className="form-control"
                                />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Ubicacion</label>
                                <input type="text" 
                                       className="form-control"
                                       required
                                />
                            </div>

                            <button className="btn btn-primary w-100">Register</button>
                        </div>

                        

                    </div>
                </div>
            </div>
        </div>
    );
}

export default Register;