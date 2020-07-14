import React from "react";
import PropTypes from "prop-types";

import "./Table.css";

const TableElement = ({
  element: { name, value, isValueBold, isNameBold, isSideBySide, isEmptyRow }
}) => {
  const columnWidth = isSideBySide ? "side-by-side" : "";
  const emptyRow = isEmptyRow ? "empty-row" : "";

  return (
    <tr colSpan="2" className={emptyRow}>
      {isNameBold && (
        <td className={columnWidth}>
          <b>{name}</b>
        </td>
      )}
      {!isNameBold && <td className={columnWidth}>{name}</td>}
      {isValueBold && (
        <td style={{ textAlign: "right" }}>
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
    name: PropTypes.oneOfType([PropTypes.string, PropTypes.object]),
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.object]),
    isValueBold: PropTypes.bool,
    isNameBold: PropTypes.bool,
    isSideBySide: PropTypes.bool,
    isEmptyRow: PropTypes.bool
  }).isRequired
};

Table.propTypes = {
  heading: PropTypes.oneOfType([PropTypes.string, PropTypes.object]),
  elements: PropTypes.arrayOf(
    PropTypes.shape({
      name: PropTypes.oneOfType([PropTypes.string, PropTypes.object])
        .isRequired,
      value: PropTypes.oneOfType([PropTypes.string, PropTypes.object])
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
