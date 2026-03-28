import { NavLink } from 'react-router-dom';

function NavBar() {
  return (
    <nav className="navbar">
      <span className="navbar-brand">Misar</span>
      <div className="navbar-links">
        <NavLink to="/" end className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
          Dashboard
        </NavLink>
        <NavLink to="/checkin" className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
          Check-In
        </NavLink>
        <NavLink to="/questions" className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
          Questions
        </NavLink>
      </div>
    </nav>
  );
}

export default NavBar;
