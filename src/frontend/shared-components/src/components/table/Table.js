import React from "react";
import PropTypes from "prop-types";

import "./Table.css";

const TableElement = ({ element: { name, value } }) => {
  return (
    <tr>
      <td>{name}</td>
      <td>{value}</td>
    </tr>
  );
};

export default function Table({ id, heading, elements, styling }) {
  const tableComponents = elements.map(element => {
    return <TableElement key={element.key || element.name} element={element} />;
  });

  return (
    <table id={id} className={styling}>
      <thead>
        <tr>
          <th colSpan="2">{heading}</th>
        </tr>
      </thead>
      <tbody>{tableComponents}</tbody>
    </table>
  );
}

TableElement.propTypes = {
  element: PropTypes.shape({
    name: PropTypes.string.isRequired,
    value: PropTypes.string.isRequired
  }).isRequired
};

Table.propTypes = {
  id: PropTypes.string,
  heading: PropTypes.oneOfType([PropTypes.string, PropTypes.object]),
  elements: PropTypes.arrayOf(
    PropTypes.shape({
      name: PropTypes.string.isRequired,
      value: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
        .isRequired
    }).isRequired
  ),
  styling: PropTypes.string
};

Table.defaultProps = {
  id: "",
  styling: "",
  heading: "",
  elements: []
};
