import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getDatabase, ref, get, set } from "firebase/database";
import { app } from "../firebase/config";

export default function UpdateWrite() {
  const { firebaseID } = useParams();
  const navigate = useNavigate();

  const [inputValue1, setInputValue1] = useState("");
  const [inputValue2, setInputValue2] = useState("");

  useEffect(() => {
    if (!firebaseID) return;

    const fetchData = async () => {
      try {
        const appOptions = app && app.options ? app.options : {};
        if (!appOptions.projectId && !appOptions.databaseURL) {
          alert(
            "Firebase is not configured for Realtime Database. Please set REACT_APP_FIREBASE_PROJECT_ID or REACT_APP_FIREBASE_DATABASE_URL in your .env"
          );
          return;
        }

        const database = getDatabase(app);
        const DBReference = ref(database, "nature/fruits/" + firebaseID);
        const snapshot = await get(DBReference);

        if (snapshot.exists()) {
          const targetObject = snapshot.val();
          setInputValue1(targetObject.fruitName || "");
          setInputValue2(targetObject.fruitDefinition || "");
        } else {
          alert("No data found for the provided ID");
        }
      } catch (error) {
        alert(`Error: ${error.message}`);
      }
    };

    fetchData();
  }, [firebaseID]);

  const overrideData = async () => {
    try {
      const appOptions = app && app.options ? app.options : {};
      if (!appOptions.projectId && !appOptions.databaseURL) {
        alert(
          "Firebase is not configured for Realtime Database. Please set REACT_APP_FIREBASE_PROJECT_ID or REACT_APP_FIREBASE_DATABASE_URL in your .env"
        );
        return;
      }

      const database = getDatabase(app);
      const DBReference = ref(database, "nature/fruits/" + firebaseID);

      set(DBReference, {
        fruitName: inputValue1,
        fruitDefinition: inputValue2,
      })
        .then(() => {
          alert("Data updated successfully");
          navigate("/updateread");
        })
        .catch((error) => {
          alert(error.message || error);
        });
    } catch (error) {
      alert(`Error: ${error.message}`);
    }
  };

  return (
    <div className="App">
      <h1>Update Record</h1>

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
        <button className="button" onClick={overrideData}>
          Update Data
        </button>
        <button className="button" onClick={() => navigate("/updateread")}>
          Back
        </button>
      </div>
    </div>
  );
}
