import React from "react";
import PropTypes from "prop-types";

import "./Table.css";

const TableElement = ({
  element: { name, value, isValueBold, isNameBold }
}) => {
  return (
    <tr>
      {isNameBold && (
        <td>
          <b>{name}</b>
        </td>
      )}
      {!isNameBold && <td>{name}</td>}
      {isValueBold && (
        <td>
          <b>{value}</b>
        </td>
      )}
      {!isValueBold && <td>{value}</td>}
    </tr>
  );
};

export const Table = ({ heading, elements, styling }) => {
  const tableComponents = elements.map(element => {
    return <TableElement key={element.key || element.name} element={element} />;
  });

  return (
    <table className={styling}>
      <thead>
        <tr>
          <th colSpan="2">{heading}</th>
        </tr>
      </thead>
      <tbody>{tableComponents}</tbody>
    </table>
  );
};

TableElement.propTypes = {
  element: PropTypes.shape({
    name: PropTypes.string.isRequired,
    value: PropTypes.string.isRequired,
    isValueBold: PropTypes.bool,
    isNameBold: PropTypes.bool
  }).isRequired
};

Table.propTypes = {
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
  styling: "",
  heading: "",
  elements: []
};
