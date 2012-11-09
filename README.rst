PahTum
======

Introduction
------------

Suubmitted in part fulfillment for the degree of MSc in Software Engineering

Department of Computer Science

The University of York

Abstract
--------

Monte Carlo Tree Search has been shown to be highly successful on very challenging games, such as, Go. The method is based on randomly sampling sequences of moves to the end of the game and assigning a score to the next move based on the outcomes of these samples. Experiments with Go have shown that biasing the sampling with heuristic expert knowledge did not help but rather hurt performance.

In this paper, we exercise the application of heuristic evaluation to Monte Carlo Tree Search algorithm for the ancient game PahTum. At first, we construct brand new heuristics and implement AI agent solely relying on it without performing any roll-outs. In the match of 100 games it outperformed UCT algorithm proposed by Kocsis and Szepesvari by winning 99% of the games. Then we demonstrate various techniques of how aforementioned heuristic evaluation function can be incorporated to the Monte Carlo Tree Search algorithm in order to improve the quality of play. The most successful implementation in the match of 100 games won over heuristic-based agent 87% of the games and 100% against UCT with 5 times smaller number of roll-outs in the match of 400 games.

Know how
--------

In order to find out how to run the code and further explanations please refer to KnowHow.pdf document.