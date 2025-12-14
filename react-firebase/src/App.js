import React from "react";
import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Write from "./components/Write";
import Read from "./components/Read";
import UpdateRead from "./components/UpdateRead";
import UpdateWrite from "./components/UpdateWrite";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Write />} />
        <Route path="/right" element={<Write />} />
        <Route path="/read" element={<Read />} />
        <Route path="/updateread" element={<UpdateRead />} />
        <Route path="/updaterite/:firebaseID" element={<UpdateWrite />} />
      </Routes>
    </Router>
  );
}

export default App;
