import React from 'react';
import { Card, Table } from 'react-bootstrap';

export const UserEvaluations = ({ evaluations }) => (
  <Card className="mb-4">
    <Card.Header as="h5">Evaluations</Card.Header>
    <Card.Body>
      <Table striped bordered hover size="sm">
        <thead>
          <tr>
            <th>Date</th>
            <th>Type</th>
            <th>Status</th>
            <th>Score</th>
          </tr>
        </thead>
        <tbody>
          {evaluations && evaluations.length > 0 ? (
            evaluations.map(ev => (
              <tr key={ev.id}>
                <td>{ev.date}</td>
                <td>{ev.type}</td>
                <td>{ev.status}</td>
                <td>{ev.score}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={4} className="text-center">
                No evaluations found
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </Card.Body>
  </Card>
);
