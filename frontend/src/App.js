import { BrowserRouter, Routes, Route } from 'react-router-dom';
import NavBar from './components/NavBar';
import DashboardPage from './pages/DashboardPage';
import CheckInPage from './pages/CheckInPage';
import QuestionsPage from './pages/QuestionsPage';
import './App.css';

function App() {
  return (
    <BrowserRouter>
      <NavBar />
      <main className="main-content">
        <Routes>
          <Route path="/" element={<DashboardPage />} />
          <Route path="/checkin" element={<CheckInPage />} />
          <Route path="/questions" element={<QuestionsPage />} />
        </Routes>
      </main>
    </BrowserRouter>
  );
}

export default App;
