# TINF20B1: Thompson's Construction Algorithm

This repository is part of a lecture series on compiler construction at the Baden-Württemberg Cooperative State University Karlsruhe. The university offers career-integrated undergraduate study programs. In alternating three-month phases, students learn theory at the university and receive practical training from an enterprise. For more information, please take a look at [the official website](https://www.karlsruhe.dhbw.de/en/general/about-dhbw-karlsruhe.html).

Obviously, this small project serves exclusively for educational purposes. There is no optimization in terms of performance or anything like that.

## Task Description

The goal of this challenge is to transform a string into a structured regular expression. The regular expression can then be used to successively generate an equivalent state machine via Thompson's construction algorithm. The automaton can ultimately validate various inputs. This is exactly what happens in the tests.

Stupidly, the transformation of the string into a regular expression was not implemented correctly. Some tests fail because of this. The code location was marked with a TODO. Implement a better algorithm so that the tests pass.
