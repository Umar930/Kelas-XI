import React, { useState } from "react";
import { getDatabase, ref, get, remove } from "firebase/database";
import { app } from "../firebase/config";
import { useNavigate } from "react-router-dom";

export default function UpdateRead() {
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
        const myData = snapshot.val(); // object with keys
        const temporaryArray = Object.keys(myData); // array of keys

        const transformed = temporaryArray.map((myFireID) => ({
          ...myData[myFireID],
          fruitID: myFireID,
        }));

        setFruitArray(transformed);
      } else {
        alert("No data available");
      }
    } catch (error) {
      alert(`Error: ${error.message}`);
    }
  };

  const deleteFruit = async (fruitIDtoDelete) => {
    try {
      const appOptions = app && app.options ? app.options : {};
      if (!appOptions.projectId && !appOptions.databaseURL) {
        alert(
          "Firebase is not configured for Realtime Database. Please set REACT_APP_FIREBASE_PROJECT_ID or REACT_APP_FIREBASE_DATABASE_URL in your .env"
        );
        return;
      }

      const database = getDatabase(app);
      const DBReference = ref(database, `nature/fruits/${fruitIDtoDelete}`);
      await remove(DBReference);
      // reload to refresh list
      window.location.reload();
    } catch (error) {
      alert(`Error deleting record: ${error.message || error}`);
    }
  };

  return (
    <div className="App">
      <h1>Halaman Update Read</h1>

      <div>
        <button className="button" onClick={() => navigate("/")}>
          Home Page
        </button>
        <button className="button" onClick={() => navigate("/read")}>
          Read
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
          <li key={item.fruitID || index}>
            <div>
              <strong>ID:</strong> {item.fruitID}
            </div>
            <div>
              <strong>Name:</strong> {item.fruitName}
            </div>
            <div>
              <strong>Definition:</strong> {item.fruitDefinition}
            </div>
            <div style={{ marginTop: 8 }}>
              <button
                className="button"
                onClick={() => navigate(`/updaterite/${item.fruitID}`)}
              >
                Update
              </button>
              <button
                className="button"
                onClick={() => deleteFruit(item.fruitID)}
                style={{ marginLeft: 8 }}
              >
                Delete
              </button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
