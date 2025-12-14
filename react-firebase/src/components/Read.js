import React, { useState } from "react";
import { getDatabase, ref, get } from "firebase/database";
import { app } from "../firebase/config";
import { useNavigate } from "react-router-dom";

export default function Read() {
  const navigate = useNavigate();
  const [fruitArray, setFruitArray] = useState([]);

  const fetchData = async () => {
    try {
      // Guard: ensure Firebase app has projectId or databaseURL so RTDB can be used
      const appOptions = app && app.options ? app.options : {};
      if (!appOptions.projectId && !appOptions.databaseURL) {
        alert(
          "Firebase is not configured for Realtime Database. Please set REACT_APP_FIREBASE_PROJECT_ID or REACT_APP_FIREBASE_DATABASE_URL in your .env"
        );
        return;
      }

      const database = getDatabase(app);
      const DBReference = ref(database, "nature/fruits");
      const snapshot = await get(DBReference);

      if (snapshot.exists()) {
        const dataObject = snapshot.val();
        const dataArray = Object.values(dataObject);
        setFruitArray(dataArray);
      } else {
        alert("No data available");
      }
    } catch (error) {
      alert(`Error: ${error.message}`);
    }
  };

  return (
    <div className="App">
      <h1>Halaman Baca (Read)</h1>

      <div>
        <button className="button" onClick={() => navigate("/updateread")}>
          Update
        </button>
        <button className="button" onClick={() => navigate("/")}>
          Home Page
        </button>
      </div>

      <br />

      <div>
        <button className="button" onClick={fetchData}>
          Fetch Data
        </button>
      </div>

      <ul style={{ textAlign: "left", marginTop: 12 }}>
        {fruitArray.map((item, index) => (
          <li key={index}>
            <strong>{item.fruitName}</strong>: {item.fruitDefinition}
          </li>
        ))}
      </ul>
    </div>
  );
}
