export default function Footer() {
  return (
    <footer className="site-footer">
      <div className="footer-inner">
        <span className="brand brand-light"><span className="brand-mark">C</span><span>CASA <small>HOTEL & RESORT</small></span></span>
        <span>Trải nghiệm nghỉ dưỡng riêng tư, tinh tế.</span>
        <span>© {new Date().getFullYear()} Casa Hotel</span>
      </div>
    </footer>
  );
}