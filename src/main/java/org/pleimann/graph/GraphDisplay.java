package org.pleimann.graph;

import org.jgraph.JGraph;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphModelAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphDisplay<V, E> extends JDialog {
    private static final Color     DEFAULT_BG_COLOR = Color.decode( "#FAFBFF" );
    private static final Dimension DEFAULT_SIZE = new Dimension( 530, 320 );

    private JPanel contentPane;

    private Graph<V, E> graph;

    public GraphDisplay(Graph<V, E> graph) {
        this.graph = graph;

        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        this.contentPane = new JPanel();
        this.contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setContentPane(this.contentPane);
    }

    private void onCancel() {
        dispose();
    }

    private void createUIComponents() {
        JGraph jgraph = new JGraph(new JGraphModelAdapter(graph));
        jgraph.setMinimumSize(DEFAULT_SIZE);
        jgraph.setBackground(DEFAULT_BG_COLOR);

        this.contentPane.add(jgraph);
    }
}
