/*
 * Copyright (c) 2016, Oracle and/or its affiliates.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided
 * with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.oracle.truffle.llvm.asm.amd64;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64AdcNodeFactory.LLVMAMD64AdcbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64AdcNodeFactory.LLVMAMD64AdclNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64AdcNodeFactory.LLVMAMD64AdcqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64AdcNodeFactory.LLVMAMD64AdcwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64AddNodeFactory.LLVMAMD64AddbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64AddNodeFactory.LLVMAMD64AddlNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64AddNodeFactory.LLVMAMD64AddqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64AddNodeFactory.LLVMAMD64AddwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64AndNodeFactory.LLVMAMD64AndbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64AndNodeFactory.LLVMAMD64AndlNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64AndNodeFactory.LLVMAMD64AndqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64AndNodeFactory.LLVMAMD64AndwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64CpuidNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64DecNodeFactory.LLVMAMD64DecbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64DecNodeFactory.LLVMAMD64DeclNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64DecNodeFactory.LLVMAMD64DecqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64DecNodeFactory.LLVMAMD64DecwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64IdivNodeFactory.LLVMAMD64IdivbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64IdivNodeFactory.LLVMAMD64IdivlNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64IdivNodeFactory.LLVMAMD64IdivqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64IdivNodeFactory.LLVMAMD64IdivwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64ImmNodeFactory.LLVMAMD64I16NodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64ImmNodeFactory.LLVMAMD64I32NodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64ImmNodeFactory.LLVMAMD64I64NodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64ImmNodeFactory.LLVMAMD64I8NodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64ImulNodeFactory.LLVMAMD64ImulbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64ImulNodeFactory.LLVMAMD64ImullNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64ImulNodeFactory.LLVMAMD64ImulqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64ImulNodeFactory.LLVMAMD64ImulwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64IncNodeFactory.LLVMAMD64IncbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64IncNodeFactory.LLVMAMD64InclNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64IncNodeFactory.LLVMAMD64IncqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64IncNodeFactory.LLVMAMD64IncwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64LoadFlagsFactory.LLVMAMD64LahfNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64MulNodeFactory.LLVMAMD64MulbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64MulNodeFactory.LLVMAMD64MullNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64MulNodeFactory.LLVMAMD64MulqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64MulNodeFactory.LLVMAMD64MulwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64NegNodeFactory.LLVMAMD64NegbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64NegNodeFactory.LLVMAMD64NeglNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64NegNodeFactory.LLVMAMD64NegqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64NegNodeFactory.LLVMAMD64NegwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64NotNodeFactory.LLVMAMD64NotbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64NotNodeFactory.LLVMAMD64NotlNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64NotNodeFactory.LLVMAMD64NotqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64NotNodeFactory.LLVMAMD64NotwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64OrNodeFactory.LLVMAMD64OrbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64OrNodeFactory.LLVMAMD64OrlNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64OrNodeFactory.LLVMAMD64OrqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64OrNodeFactory.LLVMAMD64OrwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64RdtscNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64RolNodeFactory.LLVMAMD64RolbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64RolNodeFactory.LLVMAMD64RollNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64RolNodeFactory.LLVMAMD64RolqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64RolNodeFactory.LLVMAMD64RolwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64RorNodeFactory.LLVMAMD64RorbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64RorNodeFactory.LLVMAMD64RorlNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64RorNodeFactory.LLVMAMD64RorqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64RorNodeFactory.LLVMAMD64RorwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64SalNodeFactory.LLVMAMD64SalbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64SalNodeFactory.LLVMAMD64SallNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64SalNodeFactory.LLVMAMD64SalqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64SalNodeFactory.LLVMAMD64SalwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64SarNodeFactory.LLVMAMD64SarbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64SarNodeFactory.LLVMAMD64SarlNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64SarNodeFactory.LLVMAMD64SarqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64SarNodeFactory.LLVMAMD64SarwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64SetNodeFactory.LLVMAMD64SetaNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64SetNodeFactory.LLVMAMD64SetaeNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64SetNodeFactory.LLVMAMD64SetbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64SetNodeFactory.LLVMAMD64SetbeNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64ShlNodeFactory.LLVMAMD64ShlbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64ShlNodeFactory.LLVMAMD64ShllNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64ShlNodeFactory.LLVMAMD64ShlqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64ShlNodeFactory.LLVMAMD64ShlwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64ShrNodeFactory.LLVMAMD64ShrbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64ShrNodeFactory.LLVMAMD64ShrlNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64ShrNodeFactory.LLVMAMD64ShrqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64ShrNodeFactory.LLVMAMD64ShrwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64SubNodeFactory.LLVMAMD64SubbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64SubNodeFactory.LLVMAMD64SublNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64SubNodeFactory.LLVMAMD64SubqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64SubNodeFactory.LLVMAMD64SubwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64XorNodeFactory.LLVMAMD64XorbNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64XorNodeFactory.LLVMAMD64XorlNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64XorNodeFactory.LLVMAMD64XorqNodeGen;
import com.oracle.truffle.llvm.nodes.asm.LLVMAMD64XorNodeFactory.LLVMAMD64XorwNodeGen;
import com.oracle.truffle.llvm.nodes.asm.support.LLVMAMD64ToRegisterNodeFactory.LLVMI16ToR64NodeGen;
import com.oracle.truffle.llvm.nodes.asm.support.LLVMAMD64ToRegisterNodeFactory.LLVMI32ToR64NodeGen;
import com.oracle.truffle.llvm.nodes.asm.support.LLVMAMD64ToRegisterNodeFactory.LLVMI8ToR64NodeGen;
import com.oracle.truffle.llvm.nodes.asm.support.LLVMAMD64WriteRegisterNode;
import com.oracle.truffle.llvm.nodes.asm.support.LLVMAMD64WriteRegisterNode.LLVMAMD64WriteI16RegisterNode;
import com.oracle.truffle.llvm.nodes.asm.support.LLVMAMD64WriteRegisterNode.LLVMAMD64WriteI32RegisterNode;
import com.oracle.truffle.llvm.nodes.asm.support.LLVMAMD64WriteRegisterNode.LLVMAMD64WriteI64RegisterNode;
import com.oracle.truffle.llvm.nodes.base.LLVMStructWriteNode;
import com.oracle.truffle.llvm.nodes.asm.support.LLVMAMD64AddressComputationNodeFactory.LLVMAMD64AddressDisplacementComputationNodeGen;
import com.oracle.truffle.llvm.nodes.asm.support.LLVMAMD64AddressComputationNodeFactory.LLVMAMD64AddressOffsetComputationNodeGen;
import com.oracle.truffle.llvm.nodes.asm.support.LLVMAMD64Flags;
import com.oracle.truffle.llvm.nodes.asm.support.LLVMAMD64ToI8NodeFactory.LLVMAMD64I64ToI8NodeGen;
import com.oracle.truffle.llvm.nodes.asm.support.LLVMAMD64UpdateFlagsNode;
import com.oracle.truffle.llvm.nodes.base.LLVMStructWriteNodeFactory.LLVMPrimitiveStructWriteNodeGen;
import com.oracle.truffle.llvm.nodes.cast.LLVMToI16NodeFactory.LLVMToI16NoZeroExtNodeGen;
import com.oracle.truffle.llvm.nodes.cast.LLVMToI32NodeGen.LLVMToI32NoZeroExtNodeGen;
import com.oracle.truffle.llvm.nodes.cast.LLVMToI64NodeGen.LLVMToI64NoZeroExtNodeGen;
import com.oracle.truffle.llvm.nodes.cast.LLVMToI8NodeGen.LLVMToI8NoZeroExtNodeGen;
import com.oracle.truffle.llvm.nodes.func.LLVMArgNodeGen;
import com.oracle.truffle.llvm.nodes.func.LLVMInlineAssemblyRootNode;
import com.oracle.truffle.llvm.nodes.memory.LLVMStoreNodeFactory.LLVMI16StoreNodeGen;
import com.oracle.truffle.llvm.nodes.memory.LLVMStoreNodeFactory.LLVMI32StoreNodeGen;
import com.oracle.truffle.llvm.nodes.memory.LLVMStoreNodeFactory.LLVMI64StoreNodeGen;
import com.oracle.truffle.llvm.nodes.memory.LLVMStoreNodeFactory.LLVMI8StoreNodeGen;
import com.oracle.truffle.llvm.nodes.memory.load.LLVMI16LoadNodeGen;
import com.oracle.truffle.llvm.nodes.memory.load.LLVMI32LoadNodeGen;
import com.oracle.truffle.llvm.nodes.memory.load.LLVMI64LoadNodeGen;
import com.oracle.truffle.llvm.nodes.memory.load.LLVMI8LoadNodeGen;
import com.oracle.truffle.llvm.nodes.others.LLVMUnsupportedInlineAssemblerNode;
import com.oracle.truffle.llvm.nodes.others.LLVMUnsupportedInlineAssemblerNode.LLVMI32UnsupportedInlineAssemblerNode;
import com.oracle.truffle.llvm.nodes.vars.LLVMReadNodeFactory.LLVMAddressReadNodeGen;
import com.oracle.truffle.llvm.nodes.vars.LLVMReadNodeFactory.LLVMDoubleReadNodeGen;
import com.oracle.truffle.llvm.nodes.vars.LLVMReadNodeFactory.LLVMFloatReadNodeGen;
import com.oracle.truffle.llvm.nodes.vars.LLVMReadNodeFactory.LLVMI1ReadNodeGen;
import com.oracle.truffle.llvm.nodes.vars.LLVMReadNodeFactory.LLVMI16ReadNodeGen;
import com.oracle.truffle.llvm.nodes.vars.LLVMReadNodeFactory.LLVMI32ReadNodeGen;
import com.oracle.truffle.llvm.nodes.vars.LLVMReadNodeFactory.LLVMI64ReadNodeGen;
import com.oracle.truffle.llvm.nodes.vars.LLVMReadNodeFactory.LLVMI8ReadNodeGen;
import com.oracle.truffle.llvm.nodes.vars.LLVMWriteNode.LLVMWriteAddressNode;
import com.oracle.truffle.llvm.nodes.vars.LLVMWriteNodeFactory.LLVMWriteAddressNodeGen;
import com.oracle.truffle.llvm.nodes.vars.LLVMWriteNodeFactory.LLVMWriteDoubleNodeGen;
import com.oracle.truffle.llvm.nodes.vars.LLVMWriteNodeFactory.LLVMWriteFloatNodeGen;
import com.oracle.truffle.llvm.nodes.vars.LLVMWriteNodeFactory.LLVMWriteI16NodeGen;
import com.oracle.truffle.llvm.nodes.vars.LLVMWriteNodeFactory.LLVMWriteI32NodeGen;
import com.oracle.truffle.llvm.nodes.vars.LLVMWriteNodeFactory.LLVMWriteI64NodeGen;
import com.oracle.truffle.llvm.nodes.vars.LLVMWriteNodeFactory.LLVMWriteI8NodeGen;
import com.oracle.truffle.llvm.nodes.vars.StructLiteralNode;
import com.oracle.truffle.llvm.runtime.nodes.api.LLVMExpressionNode;
import com.oracle.truffle.llvm.runtime.types.PointerType;
import com.oracle.truffle.llvm.runtime.types.PrimitiveType;
import com.oracle.truffle.llvm.runtime.types.PrimitiveType.PrimitiveKind;
import com.oracle.truffle.llvm.runtime.types.StructureType;
import com.oracle.truffle.llvm.runtime.types.Type;
import com.oracle.truffle.llvm.runtime.types.VoidType;

class AsmFactory {
    private static final int REG_START_INDEX = 1;
    private static final String TEMP_REGISTER_PREFIX = "__$$tmp_r_";

    private static final String CONSTRAINT_REG = "r";
    private static final String CONSTRAINT_ANY = "imr"; // "g" -> "imr"

    private final FrameDescriptor frameDescriptor;
    private final List<LLVMExpressionNode> statements;
    private final List<LLVMExpressionNode> arguments;
    private final List<String> registers;
    private LLVMExpressionNode result;
    private List<Argument> argInfo;
    private final String asmFlags;
    private final Type[] argTypes;
    private final Type retType;
    private final Type[] retTypes;
    private final int[] retOffsets;

    AsmFactory(Type[] argTypes, String asmFlags, Type retType, Type[] retTypes, int[] retOffsets) {
        this.argTypes = argTypes;
        this.asmFlags = asmFlags;
        this.frameDescriptor = new FrameDescriptor();
        this.statements = new ArrayList<>();
        this.arguments = new ArrayList<>();
        this.registers = new ArrayList<>();
        this.retType = retType;
        this.retTypes = retTypes;
        this.retOffsets = retOffsets;
        parseArguments();
    }

    private class Argument {
        private final boolean input;
        private final boolean output;
        private final boolean memory;
        private final Type type;
        private final int index;
        private final int inIndex;
        private final int outIndex;
        private final String source;
        private final String register;
        private final boolean anonymous;

        Argument(boolean input, boolean output, boolean memory, boolean anonymous, Type type, int index, int inIndex, int outIndex, String source, String register) {
            this.input = input;
            this.output = output;
            this.memory = memory;
            this.anonymous = anonymous;
            this.type = type;
            this.index = index;
            this.inIndex = inIndex;
            this.outIndex = outIndex;
            this.source = source;
            this.register = register;
        }

        public boolean isInput() {
            return input;
        }

        public boolean isOutput() {
            return output;
        }

        public boolean isMemory() {
            return memory;
        }

        public boolean isAnonymous() {
            return anonymous;
        }

        public Type getType() {
            return type;
        }

        public int getIndex() {
            return index;
        }

        public int getInIndex() {
            assert isInput();
            return inIndex;
        }

        public int getOutIndex() {
            assert isOutput();
            return outIndex;
        }

        public String getRegister() {
            assert isRegister();
            return register;
        }

        public boolean isRegister() {
            return register != null;
        }

        public LLVMExpressionNode getAddress() {
            assert isMemory();
            if (output) {
                return LLVMArgNodeGen.create(outIndex);
            } else {
                throw new IllegalStateException();
            }
        }

        @Override
        public String toString() {
            return String.format("Argument[IDX=%d,I=%s,O=%s,M=%s,T=%s,R=%s,S=%s]", index, input, output, memory, type, register, source);
        }
    }

    private void parseArguments() {
        argInfo = new ArrayList<>();
        String[] tokens = asmFlags.substring(1, asmFlags.length() - 1).split(",");

        int index = REG_START_INDEX + (retType instanceof StructureType ? 1 : 0);
        int outIndex = 0;

        for (String token : tokens) {
            boolean isTilde = false;
            boolean isInput = true;
            boolean isOutput = false;
            boolean isMemory = false;
            boolean isAnonymous = false;
            String source = null;
            String registerName = null;
            int i;
            for (i = 0; i < token.length() && source == null; i++) {
                switch (token.charAt(i)) {
                    case '~':
                        isTilde = true;
                        isInput = false;
                        break;
                    case '+':
                        isInput = true;
                        isOutput = true;
                        break;
                    case '=':
                        isInput = false;
                        isOutput = true;
                        break;
                    case '*':
                        isMemory = true;
                        break;
                    default:
                        source = token.substring(i);
                        break;
                }
            }

            if (isTilde) {
                continue;
            }

            int start = source.indexOf('{');
            int end = source.lastIndexOf('}');
            if (start != -1 && end != -1) {
                registerName = source.substring(start + 1, end);
            } else if (source.equals(CONSTRAINT_REG) || source.equals(CONSTRAINT_ANY)) {
                registerName = TEMP_REGISTER_PREFIX + argInfo.size();
                isAnonymous = true;
            } else if (source.length() == 1 && Character.isDigit(source.charAt(0))) {
                int id = Character.digit(source.charAt(0), 10);
                Argument arg = argInfo.get(id);
                assert isInput && !isOutput;
                isInput = true;
                isOutput = false;
                if (arg.isRegister()) {
                    registerName = arg.getRegister();
                }
                isAnonymous = arg.isAnonymous();
            }

            assert registerName == null || AsmRegisterOperand.isRegister(registerName) || registerName.startsWith(TEMP_REGISTER_PREFIX);

            int idIn = index;
            int idOut = outIndex;
            Type type;
            if (isInput) {
                type = argTypes[index++];
            } else if (retType instanceof StructureType) {
                if (isMemory) {
                    type = new PointerType(retType);
                    idOut = index++;
                } else {
                    type = retTypes[outIndex++];
                }
            } else if (isOutput) {
                type = retType;
                if (isMemory) {
                    idOut = index++;
                }
            } else {
                throw new AssertionError("neither input nor output");
            }
            if (isAnonymous && type instanceof PointerType) {
                assert registerName != null;
                addFrameSlot(registerName, type);
            }
            argInfo.add(new Argument(isInput, isOutput, isMemory, isAnonymous, type, argInfo.size(), idIn, idOut, source, registerName));
        }
        assert index == argTypes.length;
        assert retType instanceof StructureType ? outIndex == retOffsets.length : outIndex == 0;
    }

    LLVMInlineAssemblyRootNode finishInline() {
        getArguments();
        return new LLVMInlineAssemblyRootNode(null, null, frameDescriptor, statements.toArray(new LLVMExpressionNode[statements.size()]), arguments, result);
    }

    void createOperation(String operation) {
        switch (operation) {
            case "clc":
            case "cld":
            case "cli":
            case "cmc":
                statements.add(new LLVMI32UnsupportedInlineAssemblerNode(null));
                return;
            case "lahf": {
                LLVMExpressionNode lahf = LLVMAMD64LahfNodeGen.create(getFlag(LLVMAMD64Flags.CF), getFlag(LLVMAMD64Flags.PF), getFlag(LLVMAMD64Flags.ZF), getFlag(LLVMAMD64Flags.SF));
                LLVMExpressionNode write = getOperandStore(PrimitiveType.I8, new AsmRegisterOperand("ah"), lahf);
                statements.add(write);
                break;
            }
            case "popfw":
            case "pushfw":
            case "sahf":
            case "stc":
            case "std":
            case "sti":
                statements.add(new LLVMI32UnsupportedInlineAssemblerNode(null));
                return;
            case "nop":
                break;
            case "rdtsc": {
                LLVMAMD64WriteI64RegisterNode high = getRegisterStore("rdx");
                LLVMAMD64WriteI64RegisterNode low = getRegisterStore("rax");
                statements.add(LLVMAMD64RdtscNodeGen.create(low, high));
                break;
            }
            case "cpuid": {
                LLVMExpressionNode level = getOperandLoad(PrimitiveType.I32, new AsmRegisterOperand("eax"));
                LLVMAMD64WriteI32RegisterNode eax = getRegisterStore("eax");
                LLVMAMD64WriteI32RegisterNode ebx = getRegisterStore("ebx");
                LLVMAMD64WriteI32RegisterNode ecx = getRegisterStore("ecx");
                LLVMAMD64WriteI32RegisterNode edx = getRegisterStore("edx");
                statements.add(LLVMAMD64CpuidNodeGen.create(eax, ebx, ecx, edx, level));
                break;
            }
            default:
                statements.add(new LLVMI32UnsupportedInlineAssemblerNode(null));
                return;
        }
    }

    void createUnaryOperation(String operation, AsmOperand operand) {
        LLVMExpressionNode src;
        LLVMExpressionNode out;
        AsmOperand dst = operand;
        Type dstType;
        assert operation.length() > 0;
        switch (operation.charAt(operation.length() - 1)) {
            case 'b':
                src = getOperandLoad(PrimitiveType.I8, operand);
                dstType = PrimitiveType.I8;
                break;
            case 'w':
                src = getOperandLoad(PrimitiveType.I16, operand);
                dstType = PrimitiveType.I16;
                break;
            case 'l':
                src = getOperandLoad(PrimitiveType.I32, operand);
                dstType = PrimitiveType.I32;
                break;
            case 'q':
                src = getOperandLoad(PrimitiveType.I64, operand);
                dstType = PrimitiveType.I64;
                break;
            default:
                src = null;
                dstType = PrimitiveType.I64;
        }
        switch (operation) {
            case "incb":
                out = LLVMAMD64IncbNodeGen.create(getUpdateFlagsNode(), src);
                break;
            case "incw":
                out = LLVMAMD64IncwNodeGen.create(getUpdateFlagsNode(), src);
                break;
            case "incl":
                out = LLVMAMD64InclNodeGen.create(getUpdateFlagsNode(), src);
                break;
            case "incq":
                out = LLVMAMD64IncqNodeGen.create(getUpdateFlagsNode(), src);
                break;
            case "decb":
                out = LLVMAMD64DecbNodeGen.create(getUpdateFlagsNode(), src);
                break;
            case "decw":
                out = LLVMAMD64DecwNodeGen.create(getUpdateFlagsNode(), src);
                break;
            case "decl":
                out = LLVMAMD64DeclNodeGen.create(getUpdateFlagsNode(), src);
                break;
            case "decq":
                out = LLVMAMD64DecqNodeGen.create(getUpdateFlagsNode(), src);
                break;
            case "negb":
                out = LLVMAMD64NegbNodeGen.create(getUpdateFlagsNode(), src);
                break;
            case "negw":
                out = LLVMAMD64NegwNodeGen.create(getUpdateFlagsNode(), src);
                break;
            case "negl":
                out = LLVMAMD64NeglNodeGen.create(getUpdateFlagsNode(), src);
                break;
            case "negq":
                out = LLVMAMD64NegqNodeGen.create(getUpdateFlagsNode(), src);
                break;
            case "notb":
                out = LLVMAMD64NotbNodeGen.create(src);
                break;
            case "notw":
                out = LLVMAMD64NotwNodeGen.create(src);
                break;
            case "notl":
                out = LLVMAMD64NotlNodeGen.create(src);
                break;
            case "notq":
                out = LLVMAMD64NotqNodeGen.create(src);
                break;
            case "idivb":
                out = LLVMAMD64IdivbNodeGen.create(getOperandLoad(PrimitiveType.I16, new AsmRegisterOperand("ax")), src);
                dst = new AsmRegisterOperand("ax");
                dstType = PrimitiveType.I16;
                break;
            case "idivw": {
                LLVMAMD64WriteI16RegisterNode rem = getRegisterStore("dx");
                LLVMExpressionNode high = getOperandLoad(PrimitiveType.I16, new AsmRegisterOperand("dx"));
                out = LLVMAMD64IdivwNodeGen.create(rem, high, getOperandLoad(PrimitiveType.I16, new AsmRegisterOperand("ax")), src);
                dst = new AsmRegisterOperand("ax");
                dstType = PrimitiveType.I16;
                break;
            }
            case "idivl": {
                LLVMAMD64WriteI32RegisterNode rem = getRegisterStore("edx");
                LLVMExpressionNode high = getOperandLoad(PrimitiveType.I32, new AsmRegisterOperand("edx"));
                out = LLVMAMD64IdivlNodeGen.create(rem, high, getOperandLoad(PrimitiveType.I32, new AsmRegisterOperand("eax")), src);
                dst = new AsmRegisterOperand("eax");
                dstType = PrimitiveType.I32;
                break;
            }
            case "idivq": {
                LLVMAMD64WriteI64RegisterNode rem = getRegisterStore("rdx");
                LLVMExpressionNode high = getOperandLoad(PrimitiveType.I32, new AsmRegisterOperand("rdx"));
                out = LLVMAMD64IdivqNodeGen.create(rem, high, getOperandLoad(PrimitiveType.I64, new AsmRegisterOperand("rax")), src);
                dst = new AsmRegisterOperand("rax");
                dstType = PrimitiveType.I64;
                break;
            }
            case "imulb":
                out = LLVMAMD64ImulbNodeGen.create(getOperandLoad(PrimitiveType.I16, new AsmRegisterOperand("ax")), src);
                dst = new AsmRegisterOperand("ax");
                dstType = PrimitiveType.I16;
                break;
            case "imulw": {
                LLVMAMD64WriteI16RegisterNode high = getRegisterStore("dx");
                out = LLVMAMD64ImulwNodeGen.create(high, getOperandLoad(PrimitiveType.I16, new AsmRegisterOperand("ax")), src);
                dst = new AsmRegisterOperand("ax");
                dstType = PrimitiveType.I16;
                break;
            }
            case "imull": {
                LLVMAMD64WriteI32RegisterNode high = getRegisterStore("edx");
                out = LLVMAMD64ImullNodeGen.create(high, getOperandLoad(PrimitiveType.I32, new AsmRegisterOperand("eax")), src);
                dst = new AsmRegisterOperand("eax");
                dstType = PrimitiveType.I32;
                break;
            }
            case "imulq": {
                LLVMAMD64WriteI64RegisterNode high = getRegisterStore("rdx");
                out = LLVMAMD64ImulqNodeGen.create(high, getOperandLoad(PrimitiveType.I64, new AsmRegisterOperand("rax")), src);
                dst = new AsmRegisterOperand("rax");
                dstType = PrimitiveType.I64;
                break;
            }
            // TODO: implement properly
            case "divb":
                out = LLVMAMD64IdivbNodeGen.create(getOperandLoad(PrimitiveType.I16, new AsmRegisterOperand("ax")), src);
                dst = new AsmRegisterOperand("ax");
                dstType = PrimitiveType.I16;
                break;
            case "divw": {
                LLVMAMD64WriteI16RegisterNode rem = getRegisterStore("dx");
                LLVMExpressionNode high = getOperandLoad(PrimitiveType.I16, new AsmRegisterOperand("dx"));
                out = LLVMAMD64IdivwNodeGen.create(rem, high, getOperandLoad(PrimitiveType.I16, new AsmRegisterOperand("ax")), src);
                dst = new AsmRegisterOperand("ax");
                dstType = PrimitiveType.I16;
                break;
            }
            case "divl": {
                LLVMAMD64WriteI32RegisterNode rem = getRegisterStore("edx");
                LLVMExpressionNode high = getOperandLoad(PrimitiveType.I32, new AsmRegisterOperand("edx"));
                out = LLVMAMD64IdivlNodeGen.create(rem, high, getOperandLoad(PrimitiveType.I32, new AsmRegisterOperand("eax")), src);
                dst = new AsmRegisterOperand("eax");
                dstType = PrimitiveType.I32;
                break;
            }
            case "divq": {
                LLVMAMD64WriteI64RegisterNode rem = getRegisterStore("rdx");
                LLVMExpressionNode high = getOperandLoad(PrimitiveType.I32, new AsmRegisterOperand("rdx"));
                out = LLVMAMD64IdivqNodeGen.create(rem, high, getOperandLoad(PrimitiveType.I64, new AsmRegisterOperand("rax")), src);
                dst = new AsmRegisterOperand("rax");
                dstType = PrimitiveType.I64;
                break;
            }
            case "mulb":
                src = getOperandLoad(PrimitiveType.I16, operand);
                out = LLVMAMD64MulbNodeGen.create(getOperandLoad(PrimitiveType.I16, new AsmRegisterOperand("ax")), src);
                dst = new AsmRegisterOperand("ax");
                dstType = PrimitiveType.I16;
                break;
            case "mulw": {
                LLVMAMD64WriteI16RegisterNode high = getRegisterStore("dx");
                out = LLVMAMD64MulwNodeGen.create(high, getOperandLoad(PrimitiveType.I16, new AsmRegisterOperand("ax")), src);
                dst = new AsmRegisterOperand("ax");
                dstType = PrimitiveType.I16;
                break;
            }
            case "mull": {
                LLVMAMD64WriteI32RegisterNode high = getRegisterStore("edx");
                out = LLVMAMD64MullNodeGen.create(high, getOperandLoad(PrimitiveType.I32, new AsmRegisterOperand("eax")), src);
                dst = new AsmRegisterOperand("eax");
                dstType = PrimitiveType.I32;
                break;
            }
            case "mulq": {
                LLVMAMD64WriteI64RegisterNode high = getRegisterStore("rdx");
                out = LLVMAMD64MulqNodeGen.create(high, getOperandLoad(PrimitiveType.I64, new AsmRegisterOperand("rax")), src);
                dst = new AsmRegisterOperand("rax");
                dstType = PrimitiveType.I64;
                break;
            }
            case "seta":
                out = LLVMAMD64SetaNodeGen.create(getFlag(LLVMAMD64Flags.CF), getFlag(LLVMAMD64Flags.ZF));
                dstType = PrimitiveType.I8;
                break;
            case "setae":
                out = LLVMAMD64SetaeNodeGen.create(getFlag(LLVMAMD64Flags.CF));
                dstType = PrimitiveType.I8;
                break;
            case "setb":
                out = LLVMAMD64SetbNodeGen.create(getFlag(LLVMAMD64Flags.CF));
                dstType = PrimitiveType.I8;
                break;
            case "setbe":
                out = LLVMAMD64SetbeNodeGen.create(getFlag(LLVMAMD64Flags.CF), getFlag(LLVMAMD64Flags.ZF));
                dstType = PrimitiveType.I8;
                break;
            case "setc":
                out = LLVMAMD64SetbNodeGen.create(getFlag(LLVMAMD64Flags.CF));
                dstType = PrimitiveType.I8;
                break;
            default:
                statements.add(new LLVMI32UnsupportedInlineAssemblerNode(null));
                return;
        }
        LLVMExpressionNode write = getOperandStore(dstType, dst, out);
        statements.add(write);
    }

    private static boolean isShiftOperation(String operation) {
        return operation.startsWith("shl") || operation.startsWith("shr") || operation.startsWith("rol") || operation.startsWith("ror") || operation.startsWith("sal") || operation.startsWith("sar");
    }

    private static PrimitiveType getPrimitiveTypeFromSuffix(char suffix) {
        switch (suffix) {
            case 'b':
                return PrimitiveType.I8;
            case 'w':
                return PrimitiveType.I16;
            case 'l':
                return PrimitiveType.I32;
            case 'q':
                return PrimitiveType.I64;
            default:
                throw new AsmParseException("invalid size");
        }
    }

    void createBinaryOperation(String operation, AsmOperand a, AsmOperand b) {
        LLVMExpressionNode srcA;
        LLVMExpressionNode srcB;
        LLVMExpressionNode out;
        AsmOperand dst = b;
        Type dstType;
        char suffix = operation.charAt(operation.length() - 1);
        dstType = getPrimitiveTypeFromSuffix(suffix);
        srcB = getOperandLoad(dstType, b);
        if (isShiftOperation(operation)) {
            srcA = getOperandLoad(PrimitiveType.I8, a);
        } else {
            srcA = getOperandLoad(dstType, a);
        }
        switch (operation) {
            case "addb":
                out = LLVMAMD64AddbNodeGen.create(getUpdateFlagsNode(), srcA, srcB);
                break;
            case "addw":
                out = LLVMAMD64AddwNodeGen.create(getUpdateFlagsNode(), srcA, srcB);
                break;
            case "addl":
                out = LLVMAMD64AddlNodeGen.create(getUpdateFlagsNode(), srcA, srcB);
                break;
            case "addq":
                out = LLVMAMD64AddqNodeGen.create(getUpdateFlagsNode(), srcA, srcB);
                break;
            case "adcb":
                out = LLVMAMD64AdcbNodeGen.create(getUpdateFlagsNode(), srcA, srcB, getFlag(LLVMAMD64Flags.CF));
                break;
            case "adcw":
                out = LLVMAMD64AdcwNodeGen.create(getUpdateFlagsNode(), srcA, srcB, getFlag(LLVMAMD64Flags.CF));
                break;
            case "adcl":
                out = LLVMAMD64AdclNodeGen.create(getUpdateFlagsNode(), srcA, srcB, getFlag(LLVMAMD64Flags.CF));
                break;
            case "adcq":
                out = LLVMAMD64AdcqNodeGen.create(getUpdateFlagsNode(), srcA, srcB, getFlag(LLVMAMD64Flags.CF));
                break;
            case "subb":
                out = LLVMAMD64SubbNodeGen.create(srcB, srcA);
                break;
            case "subw":
                out = LLVMAMD64SubwNodeGen.create(srcB, srcA);
                break;
            case "subl":
                out = LLVMAMD64SublNodeGen.create(srcB, srcA);
                break;
            case "subq":
                out = LLVMAMD64SubqNodeGen.create(srcB, srcA);
                break;
            case "idivb":
                srcA = getOperandLoad(PrimitiveType.I8, a);
                srcB = getOperandLoad(PrimitiveType.I16, b);
                out = LLVMAMD64IdivbNodeGen.create(srcB, srcA);
                dst = new AsmRegisterOperand("ax");
                dstType = PrimitiveType.I16;
                break;
            case "idivw": {
                LLVMAMD64WriteI16RegisterNode rem = getRegisterStore("dx");
                LLVMExpressionNode high = getOperandLoad(PrimitiveType.I16, new AsmRegisterOperand("dx"));
                out = LLVMAMD64IdivwNodeGen.create(rem, high, srcB, srcA);
                dst = new AsmRegisterOperand("ax");
                break;
            }
            case "idivl": {
                LLVMAMD64WriteI32RegisterNode rem = getRegisterStore("edx");
                LLVMExpressionNode high = getOperandLoad(PrimitiveType.I32, new AsmRegisterOperand("edx"));
                out = LLVMAMD64IdivlNodeGen.create(rem, high, srcB, srcA);
                dst = new AsmRegisterOperand("eax");
                break;
            }
            case "idivq": {
                LLVMAMD64WriteI64RegisterNode rem = getRegisterStore("rdx");
                LLVMExpressionNode high = getOperandLoad(PrimitiveType.I32, new AsmRegisterOperand("rdx"));
                out = LLVMAMD64IdivqNodeGen.create(rem, high, srcB, srcA);
                dst = new AsmRegisterOperand("rax");
                break;
            }
            case "imulb":
                srcA = getOperandLoad(PrimitiveType.I16, a);
                srcB = getOperandLoad(PrimitiveType.I8, b);
                out = LLVMAMD64ImulbNodeGen.create(srcA, srcB);
                dstType = PrimitiveType.I16;
                break;
            case "imulw": {
                LLVMAMD64WriteI16RegisterNode high = getRegisterStore("dx");
                out = LLVMAMD64ImulwNodeGen.create(high, srcA, srcB);
                break;
            }
            case "imull": {
                LLVMAMD64WriteI32RegisterNode high = getRegisterStore("edx");
                out = LLVMAMD64ImullNodeGen.create(high, srcA, srcB);
                break;
            }
            case "imulq": {
                LLVMAMD64WriteI64RegisterNode high = getRegisterStore("rdx");
                out = LLVMAMD64ImulqNodeGen.create(high, srcA, srcB);
                break;
            }
            case "movb":
            case "movw":
            case "movl":
            case "movq":
                out = srcA;
                break;
            case "salb":
                out = LLVMAMD64SalbNodeGen.create(srcB, srcA);
                break;
            case "salw":
                out = LLVMAMD64SalwNodeGen.create(srcB, srcA);
                break;
            case "sall":
                out = LLVMAMD64SallNodeGen.create(srcB, srcA);
                break;
            case "salq":
                out = LLVMAMD64SalqNodeGen.create(srcB, srcA);
                break;
            case "sarb":
                out = LLVMAMD64SarbNodeGen.create(srcB, srcA);
                break;
            case "sarw":
                out = LLVMAMD64SarwNodeGen.create(srcB, srcA);
                break;
            case "sarl":
                out = LLVMAMD64SarlNodeGen.create(srcB, srcA);
                break;
            case "sarq":
                out = LLVMAMD64SarqNodeGen.create(srcB, srcA);
                break;
            case "shlb":
                out = LLVMAMD64ShlbNodeGen.create(srcB, srcA);
                break;
            case "shlw":
                out = LLVMAMD64ShlwNodeGen.create(srcB, srcA);
                break;
            case "shll":
                out = LLVMAMD64ShllNodeGen.create(srcB, srcA);
                break;
            case "shlq":
                out = LLVMAMD64ShlqNodeGen.create(srcB, srcA);
                break;
            case "shrb":
                out = LLVMAMD64ShrbNodeGen.create(srcB, srcA);
                break;
            case "shrw":
                out = LLVMAMD64ShrwNodeGen.create(srcB, srcA);
                break;
            case "shrl":
                out = LLVMAMD64ShrlNodeGen.create(srcB, srcA);
                break;
            case "shrq":
                out = LLVMAMD64ShrqNodeGen.create(srcB, srcA);
                break;
            case "rolb":
                out = LLVMAMD64RolbNodeGen.create(srcB, srcA);
                break;
            case "rolw":
                out = LLVMAMD64RolwNodeGen.create(srcB, srcA);
                break;
            case "roll":
                out = LLVMAMD64RollNodeGen.create(srcB, srcA);
                break;
            case "rolq":
                out = LLVMAMD64RolqNodeGen.create(srcB, srcA);
                break;
            case "rorb":
                out = LLVMAMD64RorbNodeGen.create(srcB, srcA);
                break;
            case "rorw":
                out = LLVMAMD64RorwNodeGen.create(srcB, srcA);
                break;
            case "rorl":
                out = LLVMAMD64RorlNodeGen.create(srcB, srcA);
                break;
            case "rorq":
                out = LLVMAMD64RorqNodeGen.create(srcB, srcA);
                break;
            case "andb":
                out = LLVMAMD64AndbNodeGen.create(getUpdateFlagsNode(), srcA, srcB);
                break;
            case "andw":
                out = LLVMAMD64AndwNodeGen.create(getUpdateFlagsNode(), srcA, srcB);
                break;
            case "andl":
                out = LLVMAMD64AndlNodeGen.create(getUpdateFlagsNode(), srcA, srcB);
                break;
            case "andq":
                out = LLVMAMD64AndqNodeGen.create(getUpdateFlagsNode(), srcA, srcB);
                break;
            case "orb":
                out = LLVMAMD64OrbNodeGen.create(srcA, srcB);
                break;
            case "orw":
                out = LLVMAMD64OrwNodeGen.create(srcA, srcB);
                break;
            case "orl":
                out = LLVMAMD64OrlNodeGen.create(srcA, srcB);
                break;
            case "orq":
                out = LLVMAMD64OrqNodeGen.create(srcA, srcB);
                break;
            case "xorb":
                out = LLVMAMD64XorbNodeGen.create(srcA, srcB);
                break;
            case "xorw":
                out = LLVMAMD64XorwNodeGen.create(srcA, srcB);
                break;
            case "xorl":
                out = LLVMAMD64XorlNodeGen.create(srcA, srcB);
                break;
            case "xorq":
                out = LLVMAMD64XorqNodeGen.create(srcA, srcB);
                break;
            default:
                statements.add(new LLVMI32UnsupportedInlineAssemblerNode(null));
                return;
        }
        LLVMExpressionNode write = getOperandStore(dstType, dst, out);
        statements.add(write);
    }

    @SuppressWarnings("unused")
    public void createTernaryOperation(String op, AsmOperand a, AsmOperand b, AsmOperand c) {
    }

    void addFrameSlot(String reg, Type type) {
        if (!registers.contains(reg)) {
            registers.add(reg);
            FrameSlotKind kind;
            if (type instanceof PrimitiveType) {
                PrimitiveKind primitiveKind = ((PrimitiveType) type).getPrimitiveKind();
                switch (primitiveKind) {
                    case I8:
                        kind = FrameSlotKind.Byte;
                        break;
                    case I32:
                        kind = FrameSlotKind.Int;
                        break;
                    case I64:
                        kind = FrameSlotKind.Long;
                        break;
                    default:
                        kind = FrameSlotKind.Illegal;
                        break;
                }
            } else if (type instanceof PointerType) {
                kind = FrameSlotKind.Object;
            } else {
                kind = FrameSlotKind.Illegal;
            }
            this.frameDescriptor.addFrameSlot(reg, type, kind);
        }
    }

    private void getArguments() {
        LLVMStructWriteNode[] writeNodes = null;
        LLVMExpressionNode[] valueNodes = null;
        if (retType instanceof StructureType) {
            writeNodes = new LLVMStructWriteNode[retTypes.length];
            valueNodes = new LLVMExpressionNode[retTypes.length];
        }

        Set<String> todoRegisters = new HashSet<>(registers);
        for (Argument arg : argInfo) {
            // output register
            if (arg.isOutput()) {
                FrameSlot slot = null;
                if (arg.isRegister()) {
                    slot = getRegisterSlot(arg.getRegister());
                    LLVMExpressionNode register = LLVMI64ReadNodeGen.create(slot);
                    if (retType instanceof StructureType) {
                        assert retTypes[arg.getOutIndex()] == arg.getType();
                        PrimitiveKind primitiveKind = ((PrimitiveType) arg.getType()).getPrimitiveKind();
                        switch (primitiveKind) {
                            case I8:
                                valueNodes[arg.getOutIndex()] = LLVMToI8NoZeroExtNodeGen.create(register);
                                writeNodes[arg.getOutIndex()] = LLVMPrimitiveStructWriteNodeGen.create();
                                break;
                            case I16:
                                valueNodes[arg.getOutIndex()] = LLVMToI16NoZeroExtNodeGen.create(register);
                                writeNodes[arg.getOutIndex()] = LLVMPrimitiveStructWriteNodeGen.create();
                                break;
                            case I32:
                                valueNodes[arg.getOutIndex()] = LLVMToI32NoZeroExtNodeGen.create(register);
                                writeNodes[arg.getOutIndex()] = LLVMPrimitiveStructWriteNodeGen.create();
                                break;
                            case I64:
                                valueNodes[arg.getOutIndex()] = register;
                                writeNodes[arg.getOutIndex()] = LLVMPrimitiveStructWriteNodeGen.create();
                                break;
                            default:
                                throw new AsmParseException("invalid operand size: " + arg.getType());
                        }
                    } else {
                        result = castResult(register);
                    }
                } else {
                    slot = getArgumentSlot(arg.getIndex(), retType);
                    if (retType instanceof PrimitiveType) {
                        handlePrimitive(slot);
                    } else if (retType instanceof StructureType) {
                        PrimitiveKind primitiveKind = ((PrimitiveType) arg.getType()).getPrimitiveKind();
                        switch (primitiveKind) {
                            case I8:
                                valueNodes[arg.getOutIndex()] = LLVMI8ReadNodeGen.create(slot);
                                writeNodes[arg.getOutIndex()] = LLVMPrimitiveStructWriteNodeGen.create();
                                break;
                            case I16:
                                valueNodes[arg.getOutIndex()] = LLVMI16ReadNodeGen.create(slot);
                                writeNodes[arg.getOutIndex()] = LLVMPrimitiveStructWriteNodeGen.create();
                                break;
                            case I32:
                                valueNodes[arg.getOutIndex()] = LLVMI32ReadNodeGen.create(slot);
                                writeNodes[arg.getOutIndex()] = LLVMPrimitiveStructWriteNodeGen.create();
                                break;
                            case I64:
                                valueNodes[arg.getOutIndex()] = LLVMI64ReadNodeGen.create(slot);
                                writeNodes[arg.getOutIndex()] = LLVMPrimitiveStructWriteNodeGen.create();
                                break;
                            default:
                                throw new AsmParseException("invalid operand size");
                        }
                    } else if (retType instanceof PointerType) {
                        result = LLVMAddressReadNodeGen.create(slot);
                    } else if (retType instanceof VoidType) {
                        result = null;
                    } else {
                        throw new AsmParseException("invalid operand size: " + retType);
                    }
                }
            }

            // input register
            if (arg.isInput()) {
                FrameSlot slot = null;
                if (arg.isRegister()) {
                    String reg = arg.isAnonymous() ? arg.getRegister() : AsmRegisterOperand.getBaseRegister(arg.getRegister());
                    slot = getRegisterSlot(reg);
                    todoRegisters.remove(reg);
                    LLVMExpressionNode argnode = LLVMArgNodeGen.create(arg.getInIndex());
                    if (argTypes[arg.getInIndex()] instanceof PointerType) {
                        arguments.add(LLVMWriteAddressNodeGen.create(argnode, slot, null));
                    } else {
                        LLVMExpressionNode node = LLVMToI64NoZeroExtNodeGen.create(argnode);
                        arguments.add(LLVMWriteI64NodeGen.create(node, slot, null));
                    }
                }
                slot = getArgumentSlot(arg.getIndex(), argTypes[arg.getInIndex()]);
                LLVMExpressionNode argnode = LLVMArgNodeGen.create(arg.getInIndex());
                if (arg.getType() instanceof PrimitiveType) {
                    handlePrimitiveArgument(arg.getType(), slot, argnode);
                } else if (arg.getType() instanceof PointerType) {
                    arguments.add(LLVMWriteAddressNodeGen.create(argnode, slot, null));
                } else {
                    throw new AsmParseException("invalid operand size: " + arg.getType());
                }
            }
        }

        if (retType instanceof StructureType) {
            LLVMExpressionNode addrArg = LLVMArgNodeGen.create(1);
            FrameSlot slot = frameDescriptor.addFrameSlot("returnValue", null, FrameSlotKind.Object);
            LLVMWriteAddressNode writeAddr = LLVMWriteAddressNodeGen.create(addrArg, slot, null);
            statements.add(writeAddr);
            LLVMExpressionNode addr = LLVMAddressReadNodeGen.create(slot);
            this.result = new StructLiteralNode(retOffsets, writeNodes, valueNodes, addr);
        }

        // initialize registers
        for (String register : todoRegisters) {
            if (register.startsWith("$")) {
                continue;
            }
            LLVMExpressionNode node = LLVMAMD64I64NodeGen.create(0);
            FrameSlot slot = getRegisterSlot(register);
            arguments.add(LLVMWriteI64NodeGen.create(node, slot, null));
        }

        assert retType instanceof VoidType || retType != null;
    }

    private void handlePrimitiveArgument(Type argType, FrameSlot slot, LLVMExpressionNode argnode) {
        switch (((PrimitiveType) argType).getPrimitiveKind()) {
            case I8:
                arguments.add(LLVMWriteI8NodeGen.create(argnode, slot, null));
                break;
            case I16:
                arguments.add(LLVMWriteI16NodeGen.create(argnode, slot, null));
                break;
            case I32:
                arguments.add(LLVMWriteI32NodeGen.create(argnode, slot, null));
                break;
            case I64:
                arguments.add(LLVMWriteI64NodeGen.create(argnode, slot, null));
                break;
            case FLOAT:
                arguments.add(LLVMWriteFloatNodeGen.create(argnode, slot, null));
                break;
            case DOUBLE:
                arguments.add(LLVMWriteDoubleNodeGen.create(argnode, slot, null));
                break;
            default:
                throw new AsmParseException("invalid operand size: " + argType);
        }
    }

    private void handlePrimitive(FrameSlot slot) {
        switch (((PrimitiveType) retType).getPrimitiveKind()) {
            case I8:
                result = LLVMI8ReadNodeGen.create(slot);
                break;
            case I16:
                result = LLVMI16ReadNodeGen.create(slot);
                break;
            case I32:
                result = LLVMI32ReadNodeGen.create(slot);
                break;
            case I64:
                result = LLVMI64ReadNodeGen.create(slot);
                break;
            case FLOAT:
                result = LLVMFloatReadNodeGen.create(slot);
                break;
            case DOUBLE:
                result = LLVMDoubleReadNodeGen.create(slot);
                break;
            default:
                throw new AsmParseException("invalid operand size: " + retType);
        }
    }

    private LLVMExpressionNode castResult(LLVMExpressionNode register) {
        switch (((PrimitiveType) retType).getPrimitiveKind()) {
            case I8:
                return LLVMToI8NoZeroExtNodeGen.create(register);
            case I16:
                return LLVMToI16NoZeroExtNodeGen.create(register);
            case I32:
                return LLVMToI32NoZeroExtNodeGen.create(register);
            case I64:
                return register;
            default:
                return new LLVMUnsupportedInlineAssemblerNode(null);
        }
    }

    private LLVMExpressionNode getOperandLoad(Type type, AsmOperand operand) {
        if (operand instanceof AsmRegisterOperand) {
            AsmRegisterOperand op = (AsmRegisterOperand) operand;
            FrameSlot frame = getRegisterSlot(op.getBaseRegister());
            LLVMExpressionNode register = LLVMI64ReadNodeGen.create(frame);
            int shift = op.getShift();
            assert type instanceof PointerType || type == op.getWidth();
            if (type instanceof PointerType) {
                return LLVMAddressReadNodeGen.create(frame);
            }
            switch (((PrimitiveType) op.getWidth()).getPrimitiveKind()) {
                case I8:
                    return LLVMAMD64I64ToI8NodeGen.create(shift, register);
                case I16:
                    return LLVMToI16NoZeroExtNodeGen.create(register);
                case I32:
                    return LLVMToI32NoZeroExtNodeGen.create(register);
                case I64:
                    return register;
                default:
                    throw new AsmParseException("unsupported operand type: " + type);
            }
        } else if (operand instanceof AsmImmediateOperand) {
            AsmImmediateOperand op = (AsmImmediateOperand) operand;
            if (op.isLabel()) {
                throw new AsmParseException("labels not supported");
            } else {
                switch (((PrimitiveType) type).getPrimitiveKind()) {
                    case I8:
                        return LLVMAMD64I8NodeGen.create((byte) op.getValue());
                    case I16:
                        return LLVMAMD64I16NodeGen.create((short) op.getValue());
                    case I32:
                        return LLVMAMD64I32NodeGen.create((int) op.getValue());
                    case I64:
                        return LLVMAMD64I64NodeGen.create(op.getValue());
                    default:
                        throw new AsmParseException("unsupported operand type: " + type);
                }
            }
        } else if (operand instanceof AsmArgumentOperand) {
            AsmArgumentOperand op = (AsmArgumentOperand) operand;
            Argument info = argInfo.get(op.getIndex());
            FrameSlot frame = getArgumentSlot(op.getIndex(), type);
            if (info.isMemory()) {
                switch (((PrimitiveType) type).getPrimitiveKind()) {
                    case I8:
                        return LLVMI8LoadNodeGen.create(LLVMAddressReadNodeGen.create(frame));
                    case I16:
                        return LLVMI16LoadNodeGen.create(LLVMAddressReadNodeGen.create(frame));
                    case I32:
                        return LLVMI32LoadNodeGen.create(LLVMAddressReadNodeGen.create(frame));
                    case I64:
                        return LLVMI64LoadNodeGen.create(LLVMAddressReadNodeGen.create(frame));
                    default:
                        throw new AsmParseException("unsupported operand type: " + type);
                }
            } else if (info.isRegister()) {
                assert type instanceof PointerType || type == info.getType();
                frame = getRegisterSlot(info.getRegister());
                if (type instanceof PointerType) {
                    return LLVMAddressReadNodeGen.create(frame);
                }
                LLVMExpressionNode register = LLVMI64ReadNodeGen.create(frame);
                switch (((PrimitiveType) type).getPrimitiveKind()) {
                    case I8:
                        return LLVMToI8NoZeroExtNodeGen.create(register);
                    case I16:
                        return LLVMToI16NoZeroExtNodeGen.create(register);
                    case I32:
                        return LLVMToI32NoZeroExtNodeGen.create(register);
                    case I64:
                        return register;
                    default:
                        throw new AsmParseException("unsupported operand type: " + type);
                }
            } else {
                throw new AssertionError("this should not happen; " + info);
            }
        } else if (operand instanceof AsmMemoryOperand) {
            AsmMemoryOperand op = (AsmMemoryOperand) operand;
            int displacement = op.getDisplacement();
            AsmOperand base = op.getBase();
            AsmOperand offset = op.getOffset();
            int shift = op.getShift();
            LLVMExpressionNode baseAddress = getOperandLoad(new PointerType(type), base);
            LLVMExpressionNode address;
            if (offset == null) {
                address = LLVMAMD64AddressDisplacementComputationNodeGen.create(displacement, baseAddress);
            } else {
                LLVMExpressionNode offsetNode = getOperandLoad(PrimitiveType.I64, base);
                address = LLVMAMD64AddressOffsetComputationNodeGen.create(displacement, shift, baseAddress, offsetNode);
            }
            switch (((PrimitiveType) type).getPrimitiveKind()) {
                case I8:
                    return LLVMI8LoadNodeGen.create(address);
                case I16:
                    return LLVMI16LoadNodeGen.create(address);
                case I32:
                    return LLVMI32LoadNodeGen.create(address);
                case I64:
                    return LLVMI64LoadNodeGen.create(address);
                default:
                    throw new AsmParseException("unsupported operand type: " + type);
            }
        }
        throw new AsmParseException("unsupported operand: " + operand);
    }

    private LLVMExpressionNode getOperandStore(Type type, AsmOperand operand, LLVMExpressionNode from) {
        if (operand instanceof AsmRegisterOperand) {
            AsmRegisterOperand op = (AsmRegisterOperand) operand;
            FrameSlot frame = getRegisterSlot(op.getBaseRegister());
            LLVMExpressionNode register = LLVMI64ReadNodeGen.create(frame);
            int shift = op.getShift();
            LLVMExpressionNode out = null;
            assert type == op.getWidth();
            switch (((PrimitiveType) op.getWidth()).getPrimitiveKind()) {
                case I8:
                    out = LLVMI8ToR64NodeGen.create(shift, register, from);
                    break;
                case I16:
                    out = LLVMI16ToR64NodeGen.create(register, from);
                    break;
                case I32:
                    out = LLVMI32ToR64NodeGen.create(register, from);
                    break;
                case I64:
                    out = from;
                    break;
                default:
                    throw new AsmParseException("unsupported operand type: " + op.getWidth());
            }
            return LLVMWriteI64NodeGen.create(out, frame, null);
        } else if (operand instanceof AsmArgumentOperand) {
            AsmArgumentOperand op = (AsmArgumentOperand) operand;
            Argument info = argInfo.get(op.getIndex());
            if (info.isMemory()) {
                LLVMExpressionNode address = info.getAddress();
                switch (((PrimitiveType) type).getPrimitiveKind()) {
                    case I8:
                        return LLVMI8StoreNodeGen.create(null, address, from);
                    case I16:
                        return LLVMI16StoreNodeGen.create(null, address, from);
                    case I32:
                        return LLVMI32StoreNodeGen.create(null, address, from);
                    case I64:
                        return LLVMI64StoreNodeGen.create(null, address, from);
                    default:
                        throw new AsmParseException("unsupported operand type: " + type);
                }
            } else if (info.isRegister()) {
                assert type == info.getType() || info.getType() instanceof PointerType;
                FrameSlot frame = getRegisterSlot(info.getRegister());
                LLVMExpressionNode register = LLVMI64ReadNodeGen.create(frame);
                LLVMExpressionNode out = null;
                if (info.getType() instanceof PointerType) {
                    throw new AsmParseException("not yet implemented: address within register");
                }
                switch (((PrimitiveType) type).getPrimitiveKind()) {
                    case I8:
                        out = LLVMI8ToR64NodeGen.create(0, register, from);
                        break;
                    case I16:
                        out = LLVMI16ToR64NodeGen.create(register, from);
                        break;
                    case I32:
                        out = LLVMI32ToR64NodeGen.create(register, from);
                        break;
                    case I64:
                        out = from;
                        break;
                    default:
                        throw new AsmParseException("unsupported operand type: " + type);
                }
                return LLVMWriteI64NodeGen.create(out, frame, null);
            } else {
                throw new AssertionError("this should not happen; " + info);
            }
        }
        throw new AsmParseException("unsupported operand type: " + operand);
    }

    @SuppressWarnings("unchecked")
    private <T extends LLVMAMD64WriteRegisterNode> T getRegisterStore(String name) {
        AsmRegisterOperand op = new AsmRegisterOperand(name);
        FrameSlot frame = getRegisterSlot(name);
        LLVMExpressionNode register = LLVMI64ReadNodeGen.create(frame);
        LLVMAMD64WriteRegisterNode node = null;
        switch (((PrimitiveType) op.getWidth()).getPrimitiveKind()) {
            case I16:
                node = new LLVMAMD64WriteI16RegisterNode(frame, register);
                break;
            case I32:
                node = new LLVMAMD64WriteI32RegisterNode(frame, register);
                break;
            case I64:
                node = new LLVMAMD64WriteI64RegisterNode(frame);
                break;
            default:
                throw new AsmParseException("unsupported operand type");
        }
        return (T) node;
    }

    private FrameSlot getRegisterSlot(String name) {
        if (name.startsWith(TEMP_REGISTER_PREFIX)) {
            addFrameSlot(name, PrimitiveType.I64);
            return frameDescriptor.findFrameSlot(name);
        }
        AsmRegisterOperand op = new AsmRegisterOperand(name);
        String baseRegister = op.getBaseRegister();
        addFrameSlot(baseRegister, PrimitiveType.I64);
        FrameSlot frame = frameDescriptor.findFrameSlot(baseRegister);
        return frame;
    }

    private static String getArgumentName(int index) {
        return "$" + index;
    }

    private FrameSlot getArgumentSlot(int index, Type type) {
        Argument info = argInfo.get(index);
        assert info.isMemory() || type instanceof StructureType || type instanceof PointerType || type == info.getType() : String.format("arg: %s; %s vs %s", info, type, info.getType());

        String name = getArgumentName(index);
        addFrameSlot(name, info.getType());
        return frameDescriptor.findFrameSlot(name);
    }

    private static String getFlagName(long flag) {
        return "$flag_" + flag;
    }

    private FrameSlot getFlagSlot(long flag) {
        String name = getFlagName(flag);
        addFrameSlot(name, PrimitiveType.I1);
        return frameDescriptor.findFrameSlot(name);
    }

    private LLVMExpressionNode getFlag(long flag) {
        return LLVMI1ReadNodeGen.create(getFlagSlot(flag));
    }

    private LLVMAMD64UpdateFlagsNode getUpdateFlagsNode() {
        return new LLVMAMD64UpdateFlagsNode(getFlagSlot(LLVMAMD64Flags.CF), getFlagSlot(LLVMAMD64Flags.PF), getFlagSlot(LLVMAMD64Flags.ZF), getFlagSlot(LLVMAMD64Flags.SF),
                        getFlagSlot(LLVMAMD64Flags.OF));
    }
}
