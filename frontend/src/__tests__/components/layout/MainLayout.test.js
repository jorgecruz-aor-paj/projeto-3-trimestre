import React from 'react';
import { screen, waitFor } from '@testing-library/react';
import { MainLayout } from 'components/layout/MainLayout';
import { render } from 'test-utils';

// Mock child components
jest.mock('components/common/Header', () => ({
  Header: () => <header data-testid="header">Header</header>,
}));

jest.mock('components/common/Footer', () => ({
  Footer: () => <footer data-testid="footer">Footer</footer>,
}));

jest.mock('components/chatbot/FloatingChatbot', () => ({
  FloatingChatbot: () => <div data-testid="chatbot">Chatbot</div>,
}));

// Mock react-router-dom Outlet
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  Outlet: () => <main data-testid="page-content">Page Content</main>,
}));

describe('MainLayout', () => {
  it('should render all layout components', async () => {
    render(<MainLayout />);

    // Wait for translations to load
    await waitFor(
      () => {
        expect(
          screen.queryByText('Loading translations...')
        ).not.toBeInTheDocument();
      },
      { timeout: 3000 }
    );

    expect(screen.getByTestId('header')).toBeInTheDocument();
    expect(screen.getByTestId('page-content')).toBeInTheDocument();
    expect(screen.getByTestId('footer')).toBeInTheDocument();
    expect(screen.getByTestId('chatbot')).toBeInTheDocument();
  });

  it('should have proper CSS classes for layout structure', async () => {
    render(<MainLayout />);

    // Wait for translations to load
    await waitFor(
      () => {
        expect(
          screen.queryByText('Loading translations...')
        ).not.toBeInTheDocument();
      },
      { timeout: 3000 }
    );

    const container = screen.getByTestId('header').parentElement;
    expect(container).toHaveClass('app-container');
    expect(container).toHaveClass('d-flex');
    expect(container).toHaveClass('flex-column');
    expect(container).toHaveClass('min-vh-100');
  });

  it('should render header at the top', async () => {
    render(<MainLayout />);

    // Wait for translations to load
    await waitFor(
      () => {
        expect(
          screen.queryByText('Loading translations...')
        ).not.toBeInTheDocument();
      },
      { timeout: 3000 }
    );

    const header = screen.getByTestId('header');
    const container = header.parentElement;
    const firstChild = container.firstChild;

    expect(firstChild).toBe(header);
  });

  it('should render main content in the middle with flex-grow', async () => {
    render(<MainLayout />);

    // Wait for translations to load
    await waitFor(
      () => {
        expect(
          screen.queryByText('Loading translations...')
        ).not.toBeInTheDocument();
      },
      { timeout: 3000 }
    );

    const pageContent = screen.getByTestId('page-content');
    const mainElement = pageContent.parentElement;

    expect(mainElement.tagName.toLowerCase()).toBe('main');
    expect(mainElement).toHaveClass('flex-grow-1');
  });

  it('should render footer at the bottom', async () => {
    render(<MainLayout />);

    // Wait for translations to load
    await waitFor(
      () => {
        expect(
          screen.queryByText('Loading translations...')
        ).not.toBeInTheDocument();
      },
      { timeout: 3000 }
    );

    const footer = screen.getByTestId('footer');
    const container = footer.parentElement;
    const children = Array.from(container.children);

    // Footer should be near the end (before chatbot)
    expect(children.indexOf(footer)).toBeGreaterThan(0);
  });

  it('should render floating chatbot', async () => {
    render(<MainLayout />);

    // Wait for translations to load
    await waitFor(
      () => {
        expect(
          screen.queryByText('Loading translations...')
        ).not.toBeInTheDocument();
      },
      { timeout: 3000 }
    );

    const chatbot = screen.getByTestId('chatbot');
    expect(chatbot).toBeInTheDocument();
  });

  it('should maintain layout structure with different content', async () => {
    render(<MainLayout />);

    // Wait for translations to load
    await waitFor(
      () => {
        expect(
          screen.queryByText('Loading translations...')
        ).not.toBeInTheDocument();
      },
      { timeout: 3000 }
    );

    // The mocked Outlet renders "Page Content", which represents dynamic content
    expect(screen.getByTestId('header')).toBeInTheDocument();
    expect(screen.getByTestId('page-content')).toBeInTheDocument();
    expect(screen.getByText('Page Content')).toBeInTheDocument();
    expect(screen.getByTestId('footer')).toBeInTheDocument();
    expect(screen.getByTestId('chatbot')).toBeInTheDocument();
  });

  it('should have accessible structure', async () => {
    render(<MainLayout />);

    // Wait for translations to load
    await waitFor(
      () => {
        expect(
          screen.queryByText('Loading translations...')
        ).not.toBeInTheDocument();
      },
      { timeout: 3000 }
    );

    // Should have proper semantic HTML structure - get the outer main element
    const mainElements = screen.getAllByRole('main');
    const outerMainElement = mainElements.find(el =>
      el.classList.contains('flex-grow-1')
    );

    expect(outerMainElement).toBeInTheDocument();
    expect(outerMainElement).toHaveClass('flex-grow-1');
  });
});
