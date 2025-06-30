import React from 'react';
import { Card, Table } from 'react-bootstrap';

export const UserTrainings = ({ trainingStats, trainings }) => (
  <Card className="mb-4">
    <Card.Header as="h5">Trainings</Card.Header>
    <Card.Body>
      <h6>Training Time Per Year</h6>
      <Table striped bordered hover size="sm">
        <thead>
          <tr>
            <th>Year</th>
            <th>Hours</th>
          </tr>
        </thead>
        <tbody>
          {trainingStats && trainingStats.length > 0 ? (
            trainingStats.map(row => (
              <tr key={row.year}>
                <td>{row.year}</td>
                <td>{row.hours}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={2} className="text-center">
                No data
              </td>
            </tr>
          )}
        </tbody>
      </Table>
      <h6>Trainings Participated</h6>
      <ul>
        {trainings && trainings.length > 0 ? (
          trainings.map(t => <li key={t.id}>{t.name}</li>)
        ) : (
          <li>No trainings found</li>
        )}
      </ul>
    </Card.Body>
  </Card>
);
