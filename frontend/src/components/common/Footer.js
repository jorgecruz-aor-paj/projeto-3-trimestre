import React from 'react';
import { Link } from 'react-router-dom';
import { Container, Row, Col } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faFacebookF,
  faTwitter,
  faLinkedinIn,
  faInstagram,
} from '@fortawesome/free-brands-svg-icons';
import { useTranslation } from 'react-i18next';

import logo from 'logo.svg';
import 'styles/components/Footer.css';

/**
 * Footer component with links, social media and copyright information
 * @returns {React.Component} Footer component
 */
const Footer = () => {
  const { t } = useTranslation();
  const currentYear = new Date().getFullYear();

  return (
    <footer className="site-footer">
      <Container fluid className="px-lg-5">
        {/* Main footer content */}
        <Row className="py-5">
          {/* Logo and description */}
          <Col lg={4} md={6} className="mb-4 mb-md-0">
            <Link
              to="/"
              className="footer-brand d-flex align-items-center mb-3"
            >
              <img
                src={logo}
                alt={t('common.appName')}
                height="36"
                className="d-inline-block align-top"
              />
              <span className="ms-2">{t('common.appName')}</span>
            </Link>
            <p className="footer-description">{t('footer.description')}</p>
            <div className="social-links">
              <a
                href="#"
                target="_blank"
                rel="noopener noreferrer"
                aria-label="Facebook"
              >
                <FontAwesomeIcon icon={faFacebookF} />
              </a>
              <a
                href="#"
                target="_blank"
                rel="noopener noreferrer"
                aria-label="Twitter"
              >
                <FontAwesomeIcon icon={faTwitter} />
              </a>
              <a
                href="#"
                target="_blank"
                rel="noopener noreferrer"
                aria-label="LinkedIn"
              >
                <FontAwesomeIcon icon={faLinkedinIn} />
              </a>
              <a
                href="#"
                target="_blank"
                rel="noopener noreferrer"
                aria-label="Instagram"
              >
                <FontAwesomeIcon icon={faInstagram} />
              </a>
            </div>
          </Col>

          {/* Quick links */}
          <Col lg={2} md={6} className="mb-4 mb-md-0">
            <h5 className="footer-heading">{t('footer.quickLinks')}</h5>
            <ul className="footer-links list-unstyled">
              <li>
                <Link to="/">{t('nav.home')}</Link>
              </li>
              <li>
                <Link to="/trainings">{t('nav.trainings')}</Link>
              </li>
              <li>
                <Link to="/evaluations">{t('nav.evaluations')}</Link>
              </li>
              <li>
                <Link to="/about">{t('nav.about')}</Link>
              </li>
            </ul>
          </Col>

          {/* Resources */}
          <Col lg={2} md={6} className="mb-4 mb-md-0">
            <h5 className="footer-heading">{t('footer.resources')}</h5>
            <ul className="footer-links list-unstyled">
              <li>
                <Link to="/faq">{t('footer.faq')}</Link>
              </li>
              <li>
                <Link to="/help">{t('footer.help')}</Link>
              </li>
              <li>
                <Link to="/blog">{t('footer.blog')}</Link>
              </li>
              <li>
                <Link to="/tutorials">{t('footer.tutorials')}</Link>
              </li>
            </ul>
          </Col>

          {/* Legal */}
          <Col lg={2} md={6} className="mb-4 mb-md-0">
            <h5 className="footer-heading">{t('footer.legal')}</h5>
            <ul className="footer-links list-unstyled">
              <li>
                <Link to="/terms">{t('footer.terms')}</Link>
              </li>
              <li>
                <Link to="/privacy">{t('footer.privacy')}</Link>
              </li>
              <li>
                <Link to="/cookies">{t('footer.cookies')}</Link>
              </li>
            </ul>
          </Col>

          {/* Contact */}
          <Col lg={2} md={6} className="mb-4 mb-md-0">
            <h5 className="footer-heading">{t('footer.contact')}</h5>
            <ul className="footer-links list-unstyled">
              <li>
                <Link to="/contact">{t('footer.contactUs')}</Link>
              </li>
              <li>
                <a href="mailto:support@example.com">{t('footer.email')}</a>
              </li>
              <li>
                <Link to="/support">{t('footer.support')}</Link>
              </li>
            </ul>
          </Col>
        </Row>

        {/* Bottom bar with copyright */}
        <Row className="border-top py-3 footer-bottom">
          <Col md={6} className="text-center text-md-start">
            <p className="mb-0">
              &copy; {currentYear} {t('common.appName')}.{' '}
              {t('footer.copyright')}
            </p>
          </Col>
          <Col md={6} className="text-center text-md-end">
            <p className="mb-0">
              {t('footer.madeWith')} <span className="text-primary">❤</span>{' '}
              {t('footer.by')} {t('common.companyName')}
            </p>
          </Col>
        </Row>
      </Container>
    </footer>
  );
};

export default Footer;
