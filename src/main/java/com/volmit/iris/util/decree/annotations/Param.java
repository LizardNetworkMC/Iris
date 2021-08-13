/*
 * Iris is a World Generator for Minecraft Bukkit Servers
 * Copyright (c) 2021 Arcane Arts (Volmit Software)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.volmit.iris.util.decree.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {
    String REQUIRED = "REQUIRED";

    String NO_ALIAS = "No Aliases Provided";

    String DEFAULT_DESCRIPTION = "No Description Provided";

    /**
     * The main name of this command.<br>
     * Required parameter.<br>
     * This is what is used in game, alongside any (if specified) {@link #aliases() aliases}
     */
    String name();

    /**
     * The description of this parameter, used in help-popups in game.<br>
     * The default value is {@link #DEFAULT_DESCRIPTION}
     */
    String description() default DEFAULT_DESCRIPTION;

    /**
     * The default value for this argument.<br>
     * The entered string is parsed to the value similarly to how commandline-text would be.<br>
     * Default is {@link #REQUIRED}, which indicates the variable MUST be defined by the person running the command.<br>
     * If you define this, the variable automatically becomes non-required, but can still be set.
     */
    String value() default REQUIRED;

    /**
     * The aliases of this parameter (instead of just the {@link #name() name} (if specified) or Method Name (name of method))<br>
     * Can be initialized as just a string (ex. "alias") or as an array (ex. {"alias1", "alias2"})<br>
     * If someone uses /plugin foo bar=baz and you specify alias="b" here, /plugin foo b=baz will do the exact same.
     */
    String[] aliases() default {NO_ALIAS};
}