import cytoscape, { CytoscapeOptions, NodeDefinition } from "cytoscape";
import { ElementsDefinition } from "cytoscape";
import { useEffect, useRef, useState } from "react";

interface Props {
  elements: ElementsDefinition;
}

export function ASTVisualizer({ elements }: Props) {
  const cyRef = useRef(null);
  const [cy, setCy] = useState<cytoscape.Core | null>(null);

  useEffect(() => {
    //Layout a tree with elk usign nPos as sibling order
    const _cy = cytoscape({
      container: cyRef.current,
      elements: {
        nodes: elements.nodes.map((e) => ({
          data: {
            ...e.data,
            terminal: e.data.terminal ? true : undefined,
          },
        })),
        edges: elements.edges,
      },
      style: [
        {
          selector: "node",
          style: {},
        },
        {
          selector: "node[label]",
          style: {
            label: "data(label)",
          },
        },
        {
          selector: "node[terminal]",
          style: {
            shape: "ellipse",
            "background-color": "#6fc3df",
          },
        },
        {
          selector: "edge",
          style: {
            "line-color": "#6fc3df",
            width: "0.5px",
          },
        },
        //Selected selector add style background-color: green
        {
          selector: ".collapsed",
          style: {
            "background-color": "green",
          },
        },
      ],
      layout: {
        name: "dagre",
        nodeDimensionsIncludeLabels: true,
        sort: (a: any, b: any) => a.data("nPos") - b.data("nPos"),
      },
      zoomingEnabled: true,
      userZoomingEnabled: true,
      zoom: 0.8336504719979256,
      minZoom: 0.125,
      maxZoom: 16,
      panningEnabled: true,
      userPanningEnabled: true,
      pan: {
        x: -17.245693955834554,
        y: -155.00970669805537,
      },
      boxSelectionEnabled: true,
      wheelSensitivity: 0.1,
      motionBlur: true,
    } as unknown as CytoscapeOptions);
    setCy(_cy);
  }, [elements]);

  useEffect(() => {
    if (cy !== null) {
      cy.on("tap", "node", function (evt) {
        //SET COLLAPSED
        const node = evt.target;
        const children = node.successors();
        const collapsed = node.hasClass("collapsed");
        if (collapsed) {
          node.removeClass("collapsed");
          children.show();
          children.removeClass("collapsed");
        } else {
          node.addClass("collapsed");
          children.hide();
        }
      });
    }
  }, [cy]);

  return <div ref={cyRef} style={{ width: "100%", height: "100%" }}></div>;
}
