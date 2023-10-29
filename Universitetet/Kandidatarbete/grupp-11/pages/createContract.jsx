import React, {Component} from "react";
import Link from "next/link";
import ConfirmationModal from "../components/admin/modal";
import Router from "next/router";
import Tree, {operations} from "../components/admin/Tree";

function AppModalWrapper() {
    return (
        <ConfirmationModal>
            {openModal => <App openModal={openModal} />}
        </ConfirmationModal>
    );
}

// TODO: make the option fritext only be available if a question is
//       NOT a branching question
class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            tree: null, // the current tree
            focused: null, // the focused element in tree
            contractName: "Avtalsnamn"
        };

        // take all pure functions in Tree.operations and convert them
        // into version that modify this component's state
        this.operations = {};
        for (const [name, fun] of Object.entries(operations)) {
            this.operations[name] = (...args) =>
                this.setState((oldState, props) => {
                    const newTree = fun.call(null, oldState.tree, ...args);
                    return {
                        tree: newTree,
                        focused: newTree && newTree.find(x => x.focused)
                    };
                });
        }
        this.operations["onClickPlus"] = this.operations.addNodeLast;
    }

    // returns the branch node of the focused one
    isFocusedBranch() {
        if (!this.state.focused) {
            return null;
        }
        return this.state.tree.getBranchParent(this.state.focused.slice());
    }
    // returns the Choice that comes after focused
    isFocusedPreChoice() {
        if (!this.state.focused) {
            return null;
        }
        return this.state.tree.isPreChoice(this.state.focused.slice());
    }

    // store/change data for the currently focused node
    handleNodeInput(event) {
        const value = event.target.value;
        const name = event.target.name;
        let focused = this.state.focused;

        if (!focused) {
            return;
        }
        this.operations.modifyNode(focused.slice(), {[name]: value});
    }

    // retreive input data from the currently focused node.
    getInputValue(name, node) {
        let path;
        if (node === undefined) {
            path = this.state.focused.slice();
        } else {
            path = this.state.tree.find(x => x === node);
        }
        return this.state.tree.get(path)[name];
    }

    onSave(e) {
        e.preventDefault();
        // NOTE: temporary debug print
        console.log(this.state.tree);
    }

    onYes = () => {
        Router.push("/admin");
    };

    render() {
        let boxClass = ["optionsClass"];
        if (this.state.focused) {
            boxClass.push("active");
        }
        // figure out what elements to include in .options
        let optionsBox = [];
        if (this.state.focused) {
            optionsBox.push(
                <div
                    style={{
                        margin: "auto",
                        width: "50%"
                    }}
                    key="question">
                    Skriv din frågetext nedan:
                    <br />
                    <textarea
                        style={{width: "100%", height: "15vh"}}
                        value={this.getInputValue("nodeQuestion")}
                        name="nodeQuestion"
                        onChange={this.handleNodeInput.bind(this)}
                    />
                </div>
            );
            optionsBox.push(
                <div
                    style={{
                        paddingTop: "3%",
                        margin: "auto",
                        width: "50%"
                    }}
                    key="expectedans">
                    Vilken typ av svar förväntas på den här frågan?:
                    <br />
                    <select
                        onChange={this.handleNodeInput.bind(this)}
                        value={this.getInputValue("nodeQuestionType")}
                        name="nodeQuestionType">
                        <option value="yesno">Ja eller Nej</option>
                        <option value="number">Ett nummer</option>
                        <option value="text">Fritext</option>
                    </select>
                </div>
            );

            const focusedBranch = this.isFocusedBranch();
            if (focusedBranch) {
                const qtype = this.getInputValue(
                    "nodeQuestionType",
                    focusedBranch
                );
                if (qtype === "yesno") {
                    optionsBox.push(
                        <div
                            style={{
                                paddingTop: "3%",
                                margin: "auto",
                                width: "50%"
                            }}
                            key={qtype}>
                            Krav på föregående:
                            <br />
                            <input
                                type="radio"
                                name="prevYesNo"
                                onChange={this.handleNodeInput.bind(this)}
                                checked={
                                    this.getInputValue("prevYesNo") === "yes"
                                }
                                value="yes"
                            />
                            Ja
                            <br />
                            <input
                                type="radio"
                                name="prevYesNo"
                                onChange={this.handleNodeInput.bind(this)}
                                checked={
                                    this.getInputValue("prevYesNo") === "no"
                                }
                                value="no"
                            />
                            Nej
                            <br />
                        </div>
                    );
                } else if (qtype === "number") {
                    optionsBox.push(
                        <div key={qtype}>
                            Siffran från föregående ska vara:
                            <select
                                onChange={this.handleNodeInput.bind(this)}
                                value={this.getInputValue("prevNumberOperator")}
                                name="prevNumberOperator">
                                <option value="=">{"="}</option>
                                <option value=">=">{">="}</option>
                                <option value="<=">{"<="}</option>
                                <option value=">">{">"}</option>
                                <option value="<">{"<"}</option>
                                <option value="!=">{"!="}</option>
                                <option value="between">
                                    mellan (inklusive)
                                </option>
                            </select>
                            <input
                                type="number"
                                name="prevNumber1"
                                onChange={this.handleNodeInput.bind(this)}
                                value={this.getInputValue("prevNumber1")}
                            />
                            {this.getInputValue("prevNumberOperator") ===
                                "between" && (
                                <span>
                                    och
                                    <input
                                        type="number"
                                        name="prevNumber2"
                                        onChange={this.handleNodeInput.bind(
                                            this
                                        )}
                                        value={this.getInputValue(
                                            "prevNumber2"
                                        )}
                                    />
                                </span>
                            )}
                        </div>
                    );
                }
            }
        }

        return (
            <div className="root">
                <div className="menu">
                    <div>
                        <button
                            onClick={() =>
                                this.props.openModal(
                                    "Är du säker på att du vill lämna den här vyn? Alla osparade ändringar kommer gå förlorade.",
                                    this.onYes
                                )
                            }>
                            Tillbaka
                        </button>
                    </div>
                    <input
                        className="upperLeftCorner"
                        name="contractName"
                        value={this.state.contractName}
                        onChange={e =>
                            this.setState({[e.target.name]: e.target.value})
                        }
                    />
                    <br />

                    <div style={{margin: "auto", paddingBottom: "0"}}>
                        <select style={{marginRight: "100px"}} name="Avsikt">
                            <option value="movetogether">Flytta ihop</option>
                            <option value="cohabitant">Inneboende</option>
                            <option value="sellcontact">Köpeavtal</option>
                        </select>
                        <select name="Entitet">
                            <option value="partner">Partner</option>
                            <option value="cohabitant">Inneboende</option>
                            <option value="item">Föremål</option>
                        </select>
                    </div>
                    <br />
                    <div className="tree">
                        <Tree
                            tree={this.state.tree}
                            handlers={this.operations}
                            popupContainer={".tree"}
                            openModal={this.props.openModal}
                        />
                    </div>
                    <div>
                        <button
                            className="saveButtonStyle"
                            onClick={this.onSave.bind(this)}>
                            Spara
                        </button>
                    </div>
                </div>
                <div className={boxClass.join(" ")}>{optionsBox}</div>
                <style jsx>
                    {`
                        :global(body) {
                            padding: 0;
                            margin: 0;
                        }
                        * {
                            box-sizing: border-box;
                        }
                        .root {
                            height: 100vh;
                            width: 100vw;
                            padding: 0.5%;
                            background-image: linear-gradient(
                                -45deg,
                                rgb(135, 206, 250),
                                white
                            );
                        }
                        .menu {
                            position: relative;
                            float: left;
                            width: 50%;
                            padding: 1%;
                            height: 100%;
                            display: flex;
                            flex-direction: column;
                        }
                        .tree {
                            position: relative;
                            height: 90%;
                            border-style: inset;
                            border: 2px solid F0EFEF;
                            border-radius: 25px;
                            padding: 0.5%;
                            overflow-x: auto;
                            overflow-y: scroll;
                            background: white;
                        }
                        .optionsClass {
                            float: left;
                            width: 50%;
                            padding: 1%;
                            height: 100%;
                            background-color: transparent;
                            border-radius: 10px 10px 10px 1000px;
                        }

                        .optionsClass.active {
                            background-image: linear-gradient(
                                -45deg,
                                skyblue,
                                white
                            );
                        }
                        .upperLeftCorner {
                            display: inline-block;
                            position: absolute;
                            padding: 1%;
                            right: 1%;
                            top: 1%;
                        }

                        .saveButtonStyle {
                            position: relative;
                            left: 1%;
                            bottom: 2%;
                        }
                        .input:hover {
                            border: #ccc;
                        }
                    `}
                </style>
            </div>
        );
    }
}

export default AppModalWrapper;
