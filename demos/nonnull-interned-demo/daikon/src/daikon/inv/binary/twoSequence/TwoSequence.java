// ***** This file is automatically generated from BinaryInvariant.java.jpp

package daikon.inv.binary.twoSequence;

import daikon.inv.*;
import daikon.inv.binary.*;
import daikon.*;
import utilMDE.Assert;
import daikon.inv.InvariantStatus;

/**
 * Base class for two variable long[] invariants.  Provides a simpler
 * mechanism for non-symmetric invariants to function over both permutations
 * of their variables.
 *
 * Non-symmetric invariants must instantiate two objects (one for each
 * argument order).  This is tracked by the variable swap.  They must always
 * access their variables via the methods var1() and var2() which return
 * the correct variable (based on the swap setting).  No other work is
 * necessary, all permuations and resurrection is handled here.
 *
 * Symmetric invariants should define symmetric() to return true or
 * override resurrect_done_swapped to do nothing.  Non-symmetric
 * invariants that use converse operations (eg, less than and greater
 * than) rather than argument swapping should override
 * resurrect_done_swapped to return the correct class.
 **/
public abstract class TwoSequence extends BinaryInvariant {
  // We are Serializable, so we specify a version to allow changes to
  // method signatures without breaking serialization.  If you add or
  // remove fields, you should change this number to the current date.
  static final long serialVersionUID = 20040113L;

  // if true, swap the order of the invariant variables
  protected boolean swap = false;

  /** Pass-through. */
  protected TwoSequence (PptSlice ppt) {
    super(ppt);
  }

  /** Returns whether or not the specified types are valid **/
  final public boolean valid_types (VarInfo[] vis) {

    if (vis.length != 2)
      return (false);

      boolean dim_ok = vis[0].file_rep_type.isArray()
                        && vis[1].file_rep_type.isArray();

    return (dim_ok && vis[0].file_rep_type.baseIsScalar()
            && vis[1].file_rep_type.baseIsScalar());
  }

  /**
   * Returns whether or not the variable order is currently swapped
   * for this invariant.
   */
  public boolean get_swap() {
    return (swap);
  }

  /**
   * Checks to see if the variable order was swapped and calls the
   * correct routine to handle it.
   */
  protected Invariant resurrect_done(int[] permutation) {
    Assert.assertTrue(permutation.length == 2);
    // Assert.assertTrue(ArraysMDE.fn_is_permutation(permutation));
    if (permutation[0] == 1)
      return resurrect_done_swapped();
    else
      return resurrect_done_unswapped();
  }

  /**
   * Swaps the variables by inverting the state of swap.
   **/
  protected Invariant resurrect_done_swapped() {
    if (!is_symmetric())
      swap = !swap;
    return this;
  }

   /**
   * Subclasses can override in the rare cases they need to fix things
   * even when not swapped.
   **/
  protected Invariant resurrect_done_unswapped() {
    // do nothing
    return this;
  }

  /**
   * Returns the first variable.  This is the only mechanism by which
   * subclasses should access variables.
   **/
  public VarInfo var1() {
    if (swap)
      return ppt.var_infos[1];
    else
      return ppt.var_infos[0];
  }

  /**
   * Returns the first variable.  This is the only mechanism by which
   * subclasses should access variables.
   **/
  public VarInfo var2() {
    if (swap)
      return ppt.var_infos[0];
    else
      return ppt.var_infos[1];
  }

  /**
   * Returns the first variable from the specified vis.  This is the
   * only mechanism by which subclasses should access variables.
   **/
  public VarInfo var1 (VarInfo[] vis) {
    if (swap)
      return vis[1];
    else
      return vis[0];
  }

  /**
   * Returns the first variable in the specified vis.  This is the
   * only mechanism by which subclasses should access variables.
   **/
  public VarInfo var2 (VarInfo[] vis) {
    if (swap)
      return vis[0];
    else
      return vis[1];
  }

  public InvariantStatus check(Object val1, Object val2, int mod_index,
                               int count) {
    // Tests for whether a value is missing should be performed before
    // making this call, so as to reduce overall work.
    Assert.assertTrue(! falsified);
    Assert.assertTrue((mod_index >= 0) && (mod_index < 4));
    long[] v1 = ((long[]) val1);
    long[] v2 = ((long[]) val2);
    if (mod_index == 0) {
      if (swap)
        return check_unmodified (v2, v1, count);
      else
        return check_unmodified(v1, v2, count);
    } else {
      if (swap)
        return check_modified (v2, v1, count);
      else
        return check_modified(v1, v2, count);
    }
  }

  public InvariantStatus add(Object val1, Object val2, int mod_index, int count) {
    // Tests for whether a value is missing should be performed before
    // making this call, so as to reduce overall work.
    Assert.assertTrue(! falsified);
    Assert.assertTrue((mod_index >= 0) && (mod_index < 4));
    long[] v1 = ((long[]) val1);
    long[] v2 = ((long[]) val2);
    if (mod_index == 0) {
      if (swap)
        return add_unmodified(v2, v1, count);
      else
        return add_unmodified(v1, v2, count);
    } else {
      if (swap)
        return add_modified(v2, v1, count);
      else
        return add_modified(v1, v2, count);
    }
  }

  public abstract InvariantStatus check_modified(long[] v1,
                                                 long[] v2, int count);

  public InvariantStatus check_unmodified(long[] v1,
                                          long[] v2, int count) {
    return InvariantStatus.NO_CHANGE;
  }

  /**
   * Default implementation simply calls check.  Subclasses can override.
   */
  public InvariantStatus add_modified(long[] v1, long[] v2, int count) {
    return (check_modified (v1, v2, count));
  }

  /**
   * By default, do nothing if the value hasn't been seen yet.
   * Subclasses can override this.
   **/
  public InvariantStatus add_unmodified(long[] v1, long[] v2, int count) {
    return InvariantStatus.NO_CHANGE;
  }

  /**
   * Returns a representation of the class.  This includes the classname,
   * variables, and swap state.
   **/
  public String repr() {
    return getClass().getName() + " (" + var1().name() + ", "
      + var2().name() + ") [swap=" + swap + "]";
  }

  /**
   * Return true if both invariants are the same class and the order
   * of the variables (swap) is the same.
   **/
  public boolean isSameFormula(Invariant other) {
    TwoSequence inv = (TwoSequence) other;
    if ((this.getClass() == inv.getClass()) && (this.swap == inv.swap))
      return (true);
    else
      return (false);
  }

  protected double computeConfidence() {
    return Invariant.conf_is_ge(ppt.num_values(), 5);
  }

}
