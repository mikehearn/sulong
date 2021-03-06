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
package com.oracle.truffle.llvm.nodes.asm;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.llvm.nodes.asm.support.LLVMAMD64Flags;
import com.oracle.truffle.llvm.runtime.nodes.api.LLVMExpressionNode;

public abstract class LLVMAMD64LoadFlags extends LLVMExpressionNode {
    @NodeChildren({@NodeChild(value = "cf", type = LLVMExpressionNode.class), @NodeChild(value = "pf", type = LLVMExpressionNode.class), @NodeChild(value = "zf", type = LLVMExpressionNode.class),
                    @NodeChild(value = "sf", type = LLVMExpressionNode.class)})
    public abstract static class LLVMAMD64LahfNode extends LLVMAMD64LoadFlags {
        @Specialization
        protected byte executeI8(boolean cf, boolean pf, boolean zf, boolean sf) {
            byte flags = 0;
            if (cf) {
                flags |= (byte) (1 << LLVMAMD64Flags.CF);
            }
            if (pf) {
                flags |= (byte) (1 << LLVMAMD64Flags.PF);
            }
            if (zf) {
                flags |= (byte) (1 << LLVMAMD64Flags.ZF);
            }
            if (sf) {
                flags |= (byte) (1 << LLVMAMD64Flags.SF);
            }
            return flags;
        }
    }
}
