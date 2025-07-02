import React, { useState } from 'react';
import { Container, Row, Col, Button, Card, Alert } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useErrorHandler } from 'hooks/useErrorHandler';

/**
 * Demo component showing how to use the error handling system
 * This component demonstrates various error scenarios and how to handle them
 */
export const ErrorHandlingDemo = () => {
  const { t } = useTranslation();
  const errorHandler = useErrorHandler();
  const [isLoading, setIsLoading] = useState(false);
  const [result, setResult] = useState(null);

  const simulateError = async errorType => {
    setIsLoading(true);
    setResult(null);

    try {
      // Simulate different types of errors
      switch (errorType) {
        case '404': {
          // Simulate 404 error
          const error404 = new Error('Not Found');
          error404.response = { status: 404 };
          throw error404;
        }

        case '403': {
          // Simulate 403 error
          const error403 = new Error('Forbidden');
          error403.response = { status: 403 };
          throw error403;
        }

        case '500': {
          // Simulate 500 error
          const error500 = new Error('Internal Server Error');
          error500.response = { status: 500 };
          throw error500;
        }

        case 'network': {
          // Simulate network error
          const networkError = new Error('Network request failed');
          networkError.name = 'NetworkError';
          throw networkError;
        }

        case 'generic':
          // Simulate generic error
          throw new Error('Something unexpected happened');

        case 'success':
          // Simulate successful request
          await new Promise(resolve => setTimeout(resolve, 1000));
          setResult('Success! Data loaded successfully.');
          break;

        default:
          throw new Error('Unknown error type');
      }
    } catch (error) {
      console.error('Demo error:', error);
      // The error will be handled by the error boundary or you can handle it manually
      errorHandler.handleError(error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDirectNavigation = route => {
    // Direct navigation to error pages for testing
    window.location.href = route;
  };

  return (
    <Container className="py-5">
      <Row>
        <Col lg={8} className="mx-auto">
          <h1 className="text-center mb-5">
            {t('errorDemo.title', 'Error Handling Demo')}
          </h1>

          <Alert variant="info" className="mb-4">
            <Alert.Heading>
              {t('errorDemo.info.title', 'How it works')}
            </Alert.Heading>
            <p>
              {t(
                'errorDemo.info.description',
                'This demo shows how the error handling system works. Click the buttons below to simulate different error scenarios.'
              )}
            </p>
          </Alert>

          {result && (
            <Alert variant="success" className="mb-4">
              {result}
            </Alert>
          )}

          <Row>
            <Col md={6} className="mb-4">
              <Card>
                <Card.Header>
                  <h5>{t('errorDemo.simulate.title', 'Simulate Errors')}</h5>
                </Card.Header>
                <Card.Body>
                  <div className="d-grid gap-2">
                    <Button
                      variant="outline-danger"
                      onClick={() => simulateError('404')}
                      disabled={isLoading}
                    >
                      {isLoading ? 'Loading...' : 'Simulate 404 Error'}
                    </Button>

                    <Button
                      variant="outline-warning"
                      onClick={() => simulateError('403')}
                      disabled={isLoading}
                    >
                      {isLoading ? 'Loading...' : 'Simulate 403 Error'}
                    </Button>

                    <Button
                      variant="outline-danger"
                      onClick={() => simulateError('500')}
                      disabled={isLoading}
                    >
                      {isLoading ? 'Loading...' : 'Simulate 500 Error'}
                    </Button>

                    <Button
                      variant="outline-info"
                      onClick={() => simulateError('network')}
                      disabled={isLoading}
                    >
                      {isLoading ? 'Loading...' : 'Simulate Network Error'}
                    </Button>

                    <Button
                      variant="outline-secondary"
                      onClick={() => simulateError('generic')}
                      disabled={isLoading}
                    >
                      {isLoading ? 'Loading...' : 'Simulate Generic Error'}
                    </Button>

                    <Button
                      variant="success"
                      onClick={() => simulateError('success')}
                      disabled={isLoading}
                    >
                      {isLoading ? 'Loading...' : 'Simulate Success'}
                    </Button>
                  </div>
                </Card.Body>
              </Card>
            </Col>

            <Col md={6} className="mb-4">
              <Card>
                <Card.Header>
                  <h5>{t('errorDemo.direct.title', 'Direct Navigation')}</h5>
                </Card.Header>
                <Card.Body>
                  <div className="d-grid gap-2">
                    <Button
                      variant="outline-primary"
                      onClick={() => handleDirectNavigation('/error/404')}
                    >
                      Go to 404 Page
                    </Button>

                    <Button
                      variant="outline-primary"
                      onClick={() => handleDirectNavigation('/error/403')}
                    >
                      Go to 403 Page
                    </Button>

                    <Button
                      variant="outline-primary"
                      onClick={() => handleDirectNavigation('/error/500')}
                    >
                      Go to 500 Page
                    </Button>

                    <Button
                      variant="outline-primary"
                      onClick={() => handleDirectNavigation('/error/network')}
                    >
                      Go to Network Error Page
                    </Button>

                    <Button
                      variant="outline-primary"
                      onClick={() => handleDirectNavigation('/error/generic')}
                    >
                      Go to Generic Error Page
                    </Button>

                    <Button
                      variant="outline-primary"
                      onClick={() => handleDirectNavigation('/offline')}
                    >
                      Go to Offline Page
                    </Button>
                  </div>
                </Card.Body>
              </Card>
            </Col>
          </Row>

          <Card className="mt-4">
            <Card.Header>
              <h5>{t('errorDemo.usage.title', 'Usage Examples')}</h5>
            </Card.Header>
            <Card.Body>
              <h6>Using the Error Handler Hook:</h6>
              <pre className="bg-light p-3 rounded">
                {`import { useErrorHandler } from 'hooks/useErrorHandler';

const { handleError, handleNotFound } = useErrorHandler();

// Handle any error
try {
  await apiCall();
} catch (error) {
  handleError(error);
}

// Direct navigation to specific error page
handleNotFound();`}
              </pre>

              <h6 className="mt-4">Using the API Service:</h6>
              <pre className="bg-light p-3 rounded">
                {`import { useApiService } from 'services/apiErrorService';

const { apiService } = useApiService();

// API calls with automatic error handling
const data = await apiService.getData('/endpoint');`}
              </pre>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};
