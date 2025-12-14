import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { getDatabase, ref, push, set } from "firebase/database";
import { app } from "../firebase/config";

export default function Write() {
  const navigate = useNavigate();
  const [inputValue1, setInputValue1] = useState("");
  const [inputValue2, setInputValue2] = useState("");

  const saveData = async () => {
    try {
      // Guard: ensure the app has necessary config for Realtime Database
      const appOptions = app && app.options ? app.options : {};
      if (!appOptions.projectId && !appOptions.databaseURL) {
        alert(
          "Firebase is not configured for Realtime Database. Please set REACT_APP_FIREBASE_PROJECT_ID or REACT_APP_FIREBASE_DATABASE_URL in your .env"
        );
        return;
      }

      const database = getDatabase(app);
      const dbRef = ref(database, "nature/fruits");
      const newDocumentRef = push(dbRef);

      set(newDocumentRef, {
        fruitName: inputValue1,
        fruitDefinition: inputValue2,
      })
        .then(() => {
          alert("Data saved successfully");
          setInputValue1("");
          setInputValue2("");
        })
        .catch((error) => {
          alert(error);
        });
    } catch (error) {
      alert(`Error: ${error.message}`);
    }
  };

  return (
    <div className="App">
      <h1>Halaman Tulis (Write)</h1>

      <div>
        <button className="button" onClick={() => navigate("/updateread")}>
          Update
        </button>
        <button className="button" onClick={() => navigate("/read")}>
          Read
        </button>
      </div>

      <br />

      <div>
        <label>
          Fruit Name:
          <input
            type="text"
            value={inputValue1}
            onChange={(e) => setInputValue1(e.target.value)}
          />
        </label>
      </div>

      <div>
        <label>
          Fruit Definition:
          <input
            type="text"
            value={inputValue2}
            onChange={(e) => setInputValue2(e.target.value)}
          />
        </label>
      </div>

      <div style={{ marginTop: 10 }}>
        <button className="button" onClick={saveData}>
          Save Data
        </button>
      </div>
    </div>
  );
}
